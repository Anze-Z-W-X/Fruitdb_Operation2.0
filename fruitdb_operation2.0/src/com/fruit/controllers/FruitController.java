package com.fruit.controllers;

import com.fruit.dao.FruitDAO;
import com.fruit.dao.impl.FruitDAOImpl;
import com.fruit.pojo.Fruit;
import com.myssm.myspringmvc.ViewBaseServlet;
import com.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class FruitController extends ViewBaseServlet {
    FruitDAO fruitDAO = new FruitDAOImpl();

    private String add(HttpServletRequest request) throws ServletException, IOException {
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        fruitDAO.addFruit(new Fruit(0,fname,price,fcount,remark));
        //response.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String del(HttpServletRequest request )throws IOException, ServletException {
        String fidStr = request.getParameter("fid");
        if(!StringUtil.isEmpty(fidStr)){
            int fid = Integer.parseInt(fidStr);
            fruitDAO.delFruit(fid);

            //super.processTemplate("index",request,response);
            //response.sendRedirect("fruit.do");
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String update(HttpServletRequest request) throws ServletException, IOException {

        String fidStr = request.getParameter("fid");
        Integer fid = Integer.parseInt(fidStr);
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        fruitDAO.updateFruit(new Fruit(fid,fname, price ,fcount ,remark ));

        //response.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }


    private String edit(HttpServletRequest request) throws ServletException, IOException {
        String fidStr = request.getCharacterEncoding();
        if(!StringUtil.isEmpty(fidStr)){
            Integer fid = Integer.parseInt(fidStr);
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit",fruit);
            //super.processTemplate("edit",req,resp);
            return "edit";
        }
        return "error";
    }

    private String index(HttpServletRequest request) throws IOException {

            HttpSession session = request.getSession() ;

            int pageNo = 1 ;

            //如果oper!=null,说明是通过搜索来的
            String oper = request.getParameter("oper");
            //
            String keyword;
            if(!StringUtil.isEmpty(oper) && "search".equals(oper)){
                keyword = request.getParameter("keyword");
                if(StringUtil.isEmpty(keyword))keyword="";
            }else {
                String pageNoStr = request.getParameter("pageNo");
                if(!StringUtil.isEmpty(pageNoStr)){
                    pageNo = Integer.parseInt(pageNoStr);
                }
                Object keywordObj = session.getAttribute("keyword");
                if(keywordObj!=null)keyword = (String)keywordObj;
                else keyword="";
            }

            session.setAttribute("pageNo",pageNo);
            session.setAttribute("keyword",keyword);
            FruitDAO fruitDAO = new FruitDAOImpl();
            List<Fruit> fruitList = fruitDAO.getFruitList(pageNo,keyword);

            session.setAttribute("fruitList",fruitList);

            //总记录条数
            int fruitCount = fruitDAO.getFruitCount(keyword);
            //总页数
            int pageCount = (fruitCount+5-1)/5 ;

            session.setAttribute("pageCount",pageCount);

            //此处的视图名称是 index
            //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
            //逻辑视图名称 ：   index
            //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
            //所以真实的视图名称是：      /       index       .html
            //super.processTemplate("index",request,response);
            return "index";

    }
}
