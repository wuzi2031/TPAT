package com.testplatform.tpat.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExcelUtil {
    /**
     * 写入数据到xls
     *
     * @param title
     * @param headers
     * @param dataList
     * @param os
     */
    public void exporteExcel(String title, String[] headers,
                             String[][] dataList, OutputStream os) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        createSheet(title, headers, dataList, workbook);
        try {
            workbook.write(os);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    /**
     * 创建xls
     *
     * @param path    路径
     * @param name    名称
     * @param title   sheet名称
     * @param headers 表头
     */
    public void createExcel(String path, String name, String title, String[] headers) {
        boolean exists = FileUtil.isFileExists(path + File.separator + name);
        if (exists) {
            FileUtil.deleteFile(new File(path + File.separator + name));
        }
        FileUtil.createDir(path);
        try {
            OutputStream os = new FileOutputStream(path + File.separator + name);
            exporteExcel(title, headers, null, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * xls追加数据
     *
     * @param path
     * @param name
     * @param dataList
     */
    public void appendData(String path, String name, String sheetName, String[][] dataList) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path + File.separator + name);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);  //使用POI提供的方法得到excel的信息
            HSSFWorkbook workBook = new HSSFWorkbook(poifsFileSystem);//得到文档对象
            HSSFSheet sheet = workBook.getSheet(sheetName);  //根据name获取sheet表
            int lastRow = sheet.getLastRowNum();
            HSSFRow row;
            for (int i = 0; i < dataList.length; i++) {
                row = sheet.createRow((short) (lastRow + 1 + i));
                for (int j = 0; j < dataList[i].length; j++) {
                    HSSFCell cell = row.createCell(j);
                    HSSFRichTextString textString = new HSSFRichTextString(
                            dataList[i][j]);// 存放每个单元格的文本
                    cell.setCellValue(textString);
                }
            }
            FileOutputStream out = new FileOutputStream(path + File.separator + name);
            out.flush();
            workBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建sheet
     *
     * @param title    sheet的名字
     * @param headers  表头
     * @param dataList 正文单元格
     */
    private void createSheet(String title, String[] headers,
                             String[][] dataList, HSSFWorkbook workBook) {
        HSSFSheet sheet = workBook.createSheet(title);
        // 设置表头和普通单元格的格式，安卓不支持
//        HSSFCellStyle headStyle = setHeaderStyle(workBook);
//        HSSFCellStyle bodyStyle = setBodyStyle(workBook);

        createBody(dataList, sheet);
        createHeader(headers, sheet);
    }

    /**
     * 创建表头
     *
     * @param headers 表头
     * @param sheet   表
     */
    private void createHeader(String[] headers, HSSFSheet sheet) {
        if (headers == null || headers.length == 0)
            return;
        HSSFRow row = sheet.createRow(0); // 创建第一行，为表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);// 这一行的每个列，即单元格

            HSSFRichTextString textString = new HSSFRichTextString(headers[i]);// 存放每个单元格的文本
            cell.setCellValue(textString); // 放进单元格

//            sheet.autoSizeColumn((short) i); // 自动设置列的大小
        }
    }

    /**
     * 创建正文单元格
     *
     * @param dataList 数据数组
     * @param sheet    表
     */
    private void createBody(String[][] dataList, HSSFSheet sheet) {
        if (dataList == null || dataList.length == 0)
            return;
        for (int a = 0; a < dataList.length; a++) {
            HSSFRow row = sheet.createRow(a + 1); // 根据数据条目创建行数
            for (int j = 0; j < dataList[a].length; j++) {
                HSSFCell cell = row.createCell(j); // 这一行的每个列，即单元格
                HSSFRichTextString textString = new HSSFRichTextString(
                        dataList[a][j]);// 存放每个单元格的文本
                cell.setCellValue(textString); // 放进单元格
            }
        }
    }

    /**
     * 设置表头格式
     *
     * @param workBook
     * @return
     */
    private HSSFCellStyle setHeaderStyle(HSSFWorkbook workBook) {
        HSSFCellStyle style = workBook.createCellStyle(); // 创建表头每个单元格格式
        style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        HSSFFont font = workBook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);

        style.setFont(font);
        return style;
    }

    /**
     * 设置正文单元格格式
     *
     * @param workBook
     * @return
     */
    private HSSFCellStyle setBodyStyle(HSSFWorkbook workBook) {
        HSSFCellStyle style2 = workBook.createCellStyle(); // 创建正文每个单元格格式
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        HSSFFont font2 = workBook.createFont();
        font2.setFontName("微软雅黑");
        style2.setFont(font2);
        return style2;
    }

    //获取表格数据
    @SuppressWarnings("unused")
    private String[][] readXls(String filePath) throws IOException {
        String[][] array2 = new String[10][3];
        InputStream in = new FileInputStream(filePath);
        @SuppressWarnings("resource")
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(in);
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }

            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }

                // 循环列Cell
                for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
                    HSSFCell hssfCell = hssfRow.getCell(cellNum);
                    if (hssfCell == null) {
                        continue;
                    }
                    array2[rowNum - 1][cellNum] = getValue(hssfCell);
                    System.out.print(getValue(hssfCell) + "   ");
                }
            }
        }
        return array2;
    }

    //读取单元格
    @SuppressWarnings({"static-access", "deprecation"})
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

}
