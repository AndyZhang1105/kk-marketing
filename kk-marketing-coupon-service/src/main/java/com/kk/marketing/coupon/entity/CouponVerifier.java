package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 券核销员信息表
 *
 * @author Zal
 * @since 2024-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon_verifier")
@Schema(name = "CouponVerifier", description = "券核销员信息表")
public class CouponVerifier extends BaseEntity {

    @Schema(description = "主键自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "券核销员名称")
    private String verifierName;

    @Schema(description = "券核销员手机")
    private String verifierPhone;

    @Schema(description = "手机号hash")
    private String verifierPhoneHash;

    @Schema(description = "手机号密文")
    private String verifierPhoneEncrypt;

    @Schema(description = "券核销员累计已核销数量")
    private Integer verifierQuantity;

    @Schema(description = "状态，1正常，0停用")
    private Integer status;
}
