using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    public class ContactInfos:IEquatable<ContactInfos>
    {
        public string EmailOrNumber { get; set; }
        public string ID { get; set; }
        public string Number { get; set; }
        public string Type { get; set; }

        public bool Equals(ContactInfos other)
        {
            ContactInfos info = (ContactInfos)other;
            return EmailOrNumber.Equals(info.EmailOrNumber) && ID.Equals(info.ID) && Number.Equals(info.Number) && Type.Equals(info.Type);
        }

        public override int GetHashCode()
        {
            return EmailOrNumber.GetHashCode()+ID.GetHashCode()+Number.GetHashCode()+Type.GetHashCode();
        }
    }
    public class Contact : IEquatable<Contact>
    {
        public List<ContactInfos> ContactInfos { get; set; }
        public string ID { get; set; }
        public string Name { get; set; }
        public string Birthday { get; set; }

        public bool Equals(Contact other)
        {
            Contact con = (Contact)other;
            return ContactInfos.Equals(con.ContactInfos) && ID.Equals(con.ID) && Name.Equals(con.Name);
        }

        public override int GetHashCode()
        {
            return ContactInfos.GetHashCode() +ID.GetHashCode() +Name.GetHashCode();
        }
    }
    public class Record : IEquatable<Record>
    {
        public string Duration { get; set; }
        public string ID { get; set; }
        public string Number { get; set; }
        public string Date { get; set; }
        public string Type { get; set; }

        public bool Equals(Record obj)
        {
            Record r = (Record)obj;
            return Duration.Equals(r.Duration) && ID.Equals(r.ID) && Number.Equals(r.Number) && Date.Equals(r.Date) && Type.Equals(r.Type);
        }

        public override int GetHashCode()
        {
            return Duration.GetHashCode() +ID.GetHashCode() +Number.GetHashCode() +Date.GetHashCode() +Type.GetHashCode();
        }
    }
    public class RootObject
    {
        public List<Record> Record { get; set; }
        public List<Contact> Contact { get; set; }
    }
}