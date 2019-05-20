using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// SetContactByUserName 的摘要说明
    /// </summary>
    public class SetContactByUserName : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String json = context.Request["Contact"];
            String sql = "select ID from user where UserName=@u;";
            MySqlParameter[] pms ={
                new MySqlParameter("@u",UserName)
            };
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql, pms);
            if (reader.HasRows)
            {
                reader.Read();
                int id = reader.GetInt32(0);
                sql = "select * from contact where User_ID=@id;";
                reader = helper.getMySqlReader(sql, new MySqlParameter("@id", id));
                MySqlHelper helper1 = new MySqlHelper();
                MySqlDataReader reader1 = null;
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        int a1 = helper1.getMySqlCom("delete from contact_info where Contact_ID=@id", new MySqlParameter("@id", reader.GetInt32(0) + ""));
                    }
                }
                int a = helper1.getMySqlCom("delete from contact where User_ID=@id", new MySqlParameter("@id", id + ""));
                
                context.Response.Write("OK"+"Contact:"+json);
            }
            else
            {
                context.Response.Write("Error");
            }
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