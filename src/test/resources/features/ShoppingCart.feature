Feature: Quản lý Giỏ hàng (Shopping Cart)

  Scenario: Thêm sản phẩm vào giỏ hàng và kiểm tra tổng tiền
    Given người dùng đăng nhập thành công với "<username>" và "<password>"
    And số lượng sản phẩm trong giỏ hàng ban đầu được ghi nhận
    
    When người dùng tìm kiếm sản phẩm "<keyword>" và chọn sản phẩm "<productName>"
    And chọn thuộc tính xuất xứ "<origin>"
    And nhấn nút "THÊM VÀO GIỎ HÀNG"
    And nhấn nút "+" để tăng số lượng lên 1
    And nhấn nút "CẬP NHẬT GIỎ HÀNG"

    Then số lượng giỏ hàng trên biểu tượng phải tăng thêm 2 đơn vị
    And tổng tiền tạm tính phải bằng đơn giá nhân với số lượng mới

     Examples:
      | username                     | password    | keyword | productName               |origin  |
      | mdangdn29@gmail.com          | D@ng291199  | merc    | Bơm nước xe Mercedes      |England |