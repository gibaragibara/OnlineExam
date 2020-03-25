package com.ptu.zxk.controller.admin;

import com.ptu.zxk.entity.admin.Question;
import com.ptu.zxk.page.admin.Page;
import com.ptu.zxk.service.admin.QuestionService;
import com.ptu.zxk.service.admin.SubjectService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ���������̨������
 */
@RequestMapping("/admin/question")
@Controller
public class QuestController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private SubjectService subjectService;
    /**
     * �����б�ҳ��
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("offset", 0);
        queryMap.put("pageSize", 999999);
        model.addObject("subjectList", subjectService.findList(queryMap));
        model.setViewName("question/list");
        return model;
    }

    /**
     * ģ��������ҳ��ʾ�б���ѯ
     *
     * @param title
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "questionType", required = false) Integer questionType,
            @RequestParam(name = "subjectId", required = false) Long subjectId,
            Page page
    ) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("title", title);
        if (questionType != null) {
            queryMap.put("questionType", questionType);
        }
        if (subjectId != null) {
            queryMap.put("subjectId", subjectId);
        }
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", questionService.findList(queryMap));
        ret.put("total", questionService.getTotal(queryMap));

        return ret;
    }


    /**
     * ��������
     *
     * @param question
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Question question) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (question == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ��������Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(question.getTitle())) {
            ret.put("type", "error");
            ret.put("msg", "����д������Ŀ��");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAnswer())) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ����ȷ�𰸣�");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAttrA())) {
            ret.put("type", "error");
            ret.put("msg", "����д����ѡ��A");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAttrB())) {
            ret.put("type", "error");
            ret.put("msg", "����д����ѡ��B");
            return ret;
        }

        question.setCreateTime(new Date());
        question.setScoreByType();

        if (questionService.add(question) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "����ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "���ӳɹ�");
        return ret;

    }

    /**
     * �༭����
     *
     * @param question
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Question question) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (question == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ��������Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(question.getTitle())) {
            ret.put("type", "error");
            ret.put("msg", "����д������Ŀ��");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAnswer())) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ����ȷ�𰸣�");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAttrA())) {
            ret.put("type", "error");
            ret.put("msg", "����д����ѡ��A");
            return ret;
        }
        if (StringUtils.isEmpty(question.getAttrB())) {
            ret.put("type", "error");
            ret.put("msg", "����д����ѡ��B");
            return ret;
        }
        question.setScoreByType();
        if (questionService.edit(question) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "�༭�ɹ�");
        return ret;
    }

    /**
     * ɾ������
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(Long id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (id == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��Ҫɾ��������");
            return ret;
        }
        try {
            if (questionService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "ɾ��ʧ�ܣ��������´��ڿ�����Ϣ������ɾ����");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "���ӳɹ�");
        return ret;
    }

    /**
     * �ϴ��ļ�������������
     *
     * @param excelFile
     * @return
     */
    @RequestMapping(value = "upload_file", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadFile(MultipartFile excelFile,Long subjectId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (excelFile == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ���ļ�");
            return ret;
        }
        if (subjectId == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������Ŀ");
            return ret;
        }
        if (excelFile.getSize() > 5000000) {
            ret.put("type", "error");
            ret.put("msg", "�ļ���С����5M");
            return ret;
        }
        String suffix = excelFile.getOriginalFilename().substring(excelFile.getOriginalFilename().lastIndexOf(".") + 1, excelFile.getOriginalFilename().length());
        if (!"xls,xlsx".contains(suffix)) {
            ret.put("type", "error");
            ret.put("msg", "���ϴ�xls��xlsx��ʽ�ļ�");
            return ret;
        }
        String msg = "����ɹ�";
        try {
            msg = readExcel(excelFile.getInputStream(),subjectId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(msg)) {
            msg = "ȫ������ɹ�";
        }
        ret.put("type", "success");
        ret.put("msg", msg);
        return ret;
    }

    /**
     * ��ȡExcel�ļ����Ҷ�ȡ�����ݿ�
     *
     * @param inputStream
     * @return
     */
    private String readExcel(InputStream inputStream,Long subjectId) {
        String msg = "";
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
            if (sheetAt.getLastRowNum() <= 0) {
                msg = "���ļ�Ϊ��";
            }
            for (int rowIndex = 1; rowIndex <= sheetAt.getLastRowNum(); rowIndex++) {
                Question question = new Question();
                HSSFRow row = sheetAt.getRow(rowIndex);
                if (row.getCell(0) == null) {
                    msg += "��" + rowIndex + "�У���������Ϊ�ա�����<br>";
                    continue;
                }
                Double numericCellValue = row.getCell(0).getNumericCellValue();
                question.setQuestionType(numericCellValue.intValue());
                if (row.getCell(1) == null) {
                    msg += "��" + rowIndex + "�У���ĿΪ�ա�����<br>";
                    continue;
                }
                question.setTitle(row.getCell(1).getStringCellValue());
                if (row.getCell(2) == null) {
                    msg += "��" + rowIndex + "�У���ĿΪ�ա�����<br>";
                    continue;
                }
                Double numericCellValue1 = row.getCell(2).getNumericCellValue();
                question.setScore(numericCellValue1.intValue());
                if (row.getCell(3) == null) {
                    msg += "��" + rowIndex + "�У�ѡ��AΪ�ա�����<br>";
                    continue;
                }
                question.setAttrA(row.getCell(3).getStringCellValue());
                if (row.getCell(4) == null) {
                    msg += "��" + rowIndex + "�У�ѡ��BΪ�ա�����<br>";
                    continue;
                }
                question.setAttrB(row.getCell(4).getStringCellValue());
                question.setAttrC(row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue());
                question.setAttrD(row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue());
                if (row.getCell(7) == null) {
                    continue;
                }
                question.setAnswer(row.getCell(7).getStringCellValue());
                question.setCreateTime(new Date());
                question.setSubjectId(subjectId);
                if (questionService.add(question) <= 0) {
                    msg += "��" + rowIndex + "�������ݿ�ʧ��<br>";
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
}