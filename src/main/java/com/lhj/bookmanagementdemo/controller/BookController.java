package com.lhj.bookmanagementdemo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.request.BookPageRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.entity.Book;
import com.lhj.bookmanagementdemo.service.IBookService;
import com.lhj.bookmanagementdemo.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin//解决跨域错误
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    IBookService bookService;
    @GetMapping("list")
    public Result list() {
        List<Book> Books = bookService.list();
        return Result.success(Books);
    }
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        Book Book = bookService.getById(id);
        return Result.success(Book);
    }
    @GetMapping("/page")
    public Result page(BookPageRequest PageRequest){
        return Result.success(bookService.page(PageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为Book对象*/Book obj){

        bookService.save(obj);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody/*将json转化为Book对象*/Book obj){
        bookService.updateById(obj);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        bookService.deleteById(id);
        return Result.success();
    }
    private static final String BASE_FILE_PATH = System.getProperty("user.dir")+"/files"+ File.separator;
    @PostMapping("/file/upload")
    //上传
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws  Exception{
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            return Result.error("文件上传失败");
        }
        long flag = System.currentTimeMillis();
        String filePath = BASE_FILE_PATH + flag + "_" + originalFilename;
try {
    FileUtil.mkParentDirs(filePath);//创建父级目录
    file.transferTo(FileUtil.file(filePath));
    Admin currentAdmin = TokenUtils.getCurrentAdmin();
    String token = TokenUtils.genToken(currentAdmin.getId().toString(), currentAdmin.getPassword(), 7);
    String url = "http://localhost:2222/api/book/file/download/"+flag +"?&token=" + token;
    if (originalFilename.endsWith(".png")|| originalFilename.endsWith(".jpg")|| originalFilename.endsWith(".jpeg")|| originalFilename.endsWith(".gif")|| originalFilename.endsWith("pdf")){
        url += "&play=1";
    }
    return Result.success(url);

    }catch (Exception e){
     log.info("文件上传失败",e);
}
     return Result.error("上传失败");

    }
    @GetMapping("/file/download/{flag}")
    public void download(@PathVariable String flag, @RequestParam(required = false) String play, HttpServletResponse response) {
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(BASE_FILE_PATH);
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");//System.currentTimeMillis()+originalFilename一个时间戳+完整的文件名
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                String realName = fileName.substring(fileName.indexOf("_") + 1);
                if ("1".equals(play)) {
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(realName, "UTF-8"));
                } else {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                }
                byte[] bytes = FileUtil.readBytes(BASE_FILE_PATH + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }
}
