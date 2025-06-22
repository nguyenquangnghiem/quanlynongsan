<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<div class="container py-5" style="max-width: 900px;">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="mb-4 text-success">Đăng bán sản phẩm</h3>
            <!-- multipart/form-data để gửi file -->
            <form action="${pageContext.request.contextPath}/secured/user/sell-product" method="POST" enctype="multipart/form-data">
                
                <input hidden name="action" value="create"/>
                <div class="mb-3">
                    <label for="name" class="form-label">Tên sản phẩm</label>
                    <input type="text" class="form-control" id="name" name="name" required placeholder="Ví dụ: Dưa hấu ruột đỏ">
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả sản phẩm</label>
                    <textarea class="form-control" id="description" name="description" rows="3" placeholder="Mô tả ngắn gọn về sản phẩm..."></textarea>
                </div>

                <div class="mb-3">
                    <label for="place" class="form-label">Nơi sản xuất</label>
                    <input type="text" class="form-control" id="place" name="place_of_manufacture" required placeholder="Ví dụ: Long An, Việt Nam">
                </div>
                <div class="mb-3">
                    <label for="categories" class="form-label">Danh mục sản phẩm</label>
                    <select class="form-select" id="categories" name="category_ids" multiple required>
                        <option value="1">Rau</option>
                        <option value="2">Cá</option>
                        <option value="3">Thịt</option>
                        <option value="4">Trái cây</option>
                    </select>
                    <div class="form-text">Giữ Ctrl (hoặc Cmd trên Mac) để chọn nhiều danh mục</div>
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Đăng bán</label>
                    <select class="form-select" name="is_sell" required>
                        <option value="true" >Có</option>
                        <option value="false" >Không</option>
                    </select>
                </div>
                    
                <div class="mb-3">
                    <label for="price" class="form-label">Giá tiền (VNĐ)</label>
                    <input type="number" class="form-control" id="price" name="price" required min="0" placeholder="Ví dụ: 25000">
                </div>

                <div class="mb-3">
                    <label for="status" class="form-label">Trạng thái</label>
                    <select class="form-select" id="status" name="status" required>
                        <option value="">-- Chọn trạng thái --</option>
                        <option value="BINH_THUONG">Bình thường</option>
                        <option value="SAP_HET_HAN">Sắp hết hạn</option>
                        <option value="HET_HAN">Hết hạn</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="quantity" class="form-label">Số lượng</label>
                    <input type="number" class="form-control" id="quantity" name="quantity" required min="1" placeholder="Nhập số lượng sản phẩm">
                </div>

                <div class="mb-3">
                    <label for="images" class="form-label">Hình ảnh sản phẩm</label>
                    <input type="file" class="form-control" id="images" name="images" accept="image/*" multiple required>
                    <div class="form-text">Bạn có thể chọn nhiều ảnh (jpg, png...)</div>
                </div>

                <!-- Hidden fields mặc định -->
                <input type="hidden" name="is_active" value="true">
                <input type="hidden" name="is_browse" value="false">

                <div class="d-grid mt-4">
                    <button type="submit" class="btn btn-success">Gửi duyệt đăng bán</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp" />
