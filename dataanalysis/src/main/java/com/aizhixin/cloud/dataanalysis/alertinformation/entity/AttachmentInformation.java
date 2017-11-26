package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Entity
@Table(name = "T_ATTACHMENT_INFORMATION")
@ToString
public class AttachmentInformation extends AbstractEntity {
    /*
    *  附件名称
   */
    @Column(name = "ATTACHMENT_NAME")
    @Getter
    @Setter
    private String attachmentName;

    /*
      *  附件类型
     */
    @Column(name = "ATTACHMENT_TYPE")
    @Getter
    @Setter
    private String attachmentType;

      /*
       *  附件地址
       */
    @Column(name = "ATTACHMENT_PATH")
    @Getter @Setter private String attachmentPath;

    /*
     *  附件来源
     */
    @Column(name = "ATTACHMENT_SOURCE")
    @Getter @Setter private String attachmentSource;

    /*
     *  上传人id
     */
    @Column(name = "UPLOAD_PEOPLE_ID")
    @Getter @Setter private Long uploadPeopleId;

    /*
     * 机构id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

      /*
       *  预警信息ID
       */
    @Column(name = "WARNINGINFORMATION_ID")
    @Getter @Setter private String warningInformationId;


}
