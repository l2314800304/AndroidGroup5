using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// UpdateContact 的摘要说明
    /// </summary>
    public class UpdateContact : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            int Contact_ID = Convert.ToInt32(context.Request["Contact_ID"]);
            String contact = context.Request["Contact"];
            String birthday = context.Request["Birthday"];
            Contact cont = JsonConvert.DeserializeObject<Contact>(contact);
            MySqlHelper helper = new MySqlHelper();
            helper.getMySqlCom("DELETE FROM Contact_Info where Contact_ID=" + Contact_ID);
            if (helper.getMySqlCom("UPDATE Contact SET Name='" + cont.Name + "',Birthday='" + birthday + "'  where Contact_ID=" + Contact_ID) > 0)
            {
                String sql = "insert into contact_info (EmailOrNumber,Number,Type,Contact_ID) values ";
                if (cont.ContactInfos != null)
                {
                    for (int j = 0; j < cont.ContactInfos.Count; j++)
                    {
                        if (sql.Length < 75)
                        {
                            sql += "(" + cont.ContactInfos[j].EmailOrNumber + ",\"" + cont.ContactInfos[j].Number + "\",\"" + cont.ContactInfos[j].Type + "\"," + Contact_ID + ")";
                        }
                        else
                        {
                            sql += ",(" + cont.ContactInfos[j].EmailOrNumber + ",\"" + cont.ContactInfos[j].Number + "\",\"" + cont.ContactInfos[j].Type + "\"," + Contact_ID + ")";
                        }
                        if (j == cont.ContactInfos.Count - 1)
                            if (helper.getMySqlCom(sql) > 0)
                                context.Response.Write("OK");
                            else
                                context.Response.Write("Error");
                    }
                }
                else
                {
                    context.Response.Write("OK");
                }
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