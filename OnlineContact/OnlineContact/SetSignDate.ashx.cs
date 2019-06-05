using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// SetSignDate 的摘要说明
    /// </summary>
    public class SetSignDate : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String sql = "select Sign,SignCount from user where UserName='" + UserName + "'";
            MySqlHelper helper = new MySqlHelper();
            MySqlDataReader reader = helper.getMySqlReader(sql);
            reader.Read();
            String Sign = ((reader.IsDBNull(0)?"":reader.GetString(0)).Equals("")?"": (reader.GetString(0)+",")+ DateTime.Today.Year+"-"+ DateTime.Today.Month+"-"+DateTime.Today.Day);
            int SignCount = reader.IsDBNull(1)?1:reader.GetInt32(1)+1;
            reader.Dispose();
            helper.mysqlcom.Dispose();
            helper.mysqlcon.Close();
            helper.mysqlcon.Dispose();
            if(helper.getMySqlCom("Update User set Sign='"+Sign+"',SignCount="+SignCount+ " where UserName='" + UserName + "'") > 0)
            {
                context.Response.Write("Sign:" + Sign + ";SignCount:" + SignCount);
            }
            else
            {
                context.Response.Write("ERROR");
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