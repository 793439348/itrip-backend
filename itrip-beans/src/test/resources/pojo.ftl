package cn.itrip.beans.pojo;

import java.io.Serializable;
import java.util.Date;

public class ${table.className} implements Serializable {

    <#list table.columns as column>
    private ${column.javaType} ${column.fileName}; <#if column.remarks !=''>// ${column.remarks}</#if>
    </#list>

    <#list table.columns as column>
    public ${column.javaType} get${column.upperFiledName}() {
        return ${column.fileName};
    }

    public void set${column.upperFiledName}(${column.javaType} ${column.fileName}) {
        this.${column.fileName} = ${column.fileName};
    }
    </#list>
}
