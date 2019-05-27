using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// GetSignDate 的摘要说明
    /// </summary>
    public class GetSignDate : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String sql = "select * from user where UserName='"+UserName+"'";
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql);
            reader.Read();
            context.Response.Write("Sign:" + (reader.IsDBNull(6)?"":reader.GetString(6)) + ";SignCount:" + reader.GetInt32(7));
            reader.Dispose();
            helper.mysqlcom.Dispose();
            helper.mysqlcon.Close();
            helper.mysqlcon.Dispose();

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