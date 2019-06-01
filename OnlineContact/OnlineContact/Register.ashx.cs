using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// 账号注册
    /// </summary>
    public class Register : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            //获取请求参数
            String UserName = context.Request["UserName"],
                Password = context.Request["Password"],
                Sex = context.Request["Sex"],
                Location = context.Request["Location"],
                Remark = context.Request["Remark"];
            //构建数据库操作语句
            String sql = "insert into user (UserName,Password,Sex,Location,Remark,Signcount) Values (@u,@p,@s,@l,@r,@c);";
            //构建参数数组
            MySqlParameter[] pms={
                new MySqlParameter("@u",UserName),
                new MySqlParameter("@p", Password),
                new MySqlParameter("@s", Sex),
                new MySqlParameter("@l", Location),
                new MySqlParameter("@r", Remark),
                new MySqlParameter("@c", 0)
            };
            //创建帮助类（自己写的）
            MySqlHelper helper = new MySqlHelper();
            //调用帮助类中的方法
            //getMySqlReader()对数据库进行查询，返回MySqlDataReader
            //getMySqlCom()对数据库进行增删改操作，返回影响行数
            //进行相关判断
            if (helper.getMySqlCom(sql, pms)>0)
            {
                //返回响应信息
                context.Response.Write("OK");
            }
            else
            {
                context.Response.Write("Error");
            }
            //进行测试
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}