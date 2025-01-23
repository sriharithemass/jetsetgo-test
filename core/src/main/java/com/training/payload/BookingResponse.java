package com.training.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookingResponse {
    private List<BookingDTO> bookingDTOList = new ArrayList<>();

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Boolean lastPage;
}
