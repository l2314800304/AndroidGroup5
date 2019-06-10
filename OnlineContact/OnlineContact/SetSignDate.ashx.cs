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
            String today = DateTime.Today.Year + "-" + (DateTime.Today.Month > 9 ? ""+DateTime.Today.Month : "0"+DateTime.Today.Month) + "-" + (DateTime.Today.Hour<8?DateTime.Today.Day-1: DateTime.Today.Day);
            String ss = reader.IsDBNull(0) ? "" : reader.GetString(0);
            String Sign = ((ss.Equals("")?"": (reader.GetString(0)+","))+ today);
            int SignCount = reader.IsDBNull(1)?1:reader.GetInt32(1)+1;
            helper.mysqlcom.Dispose();
            helper.mysqlcon.Close();
            helper.mysqlcon.Dispose();
            if(SignCount != 1)
            if (ss.Split(',')[SignCount - 2].Equals(today))
            {
                reader.Dispose();
                context.Response.Write("Sign:" + Sign + ";SignCount:" + (SignCount-1));
                return;
            }
            reader.Dispose();
            if (helper.getMySqlCom("Update User set Sign='"+Sign+"',SignCount="+SignCount+ " where UserName='" + UserName + "'") > 0)
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