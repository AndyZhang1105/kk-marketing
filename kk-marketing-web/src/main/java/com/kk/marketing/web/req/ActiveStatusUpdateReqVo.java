package com.kk.marketing.web.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ActiveStatusUpdateReqVo implements Serializable {

    @NotNull(message = "id不能不为空")
    private Long id;

}
