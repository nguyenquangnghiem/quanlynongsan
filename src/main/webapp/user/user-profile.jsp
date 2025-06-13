<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/header.jsp" />



<c:if test="${not empty message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
        
<div class="container py-4" style="max-width: 900px;">
    <!-- Cập nhật thông tin -->
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <h5 class="mb-3">Cập nhật thông tin</h5>
            <form action="${pageContext.request.contextPath}/secured/user/profile?action=updateInformation" method="post" class="row g-3 align-items-center">
                <div class="col-md-8">
                    <div class="mb-3">
                        <label for="fullname" class="form-label">Họ và tên</label>
                        <input type="text" class="form-control" id="fullname" name="fullName" value="${sessionScope.user.fullName}">
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="${sessionScope.user.email}">
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" id="phone" name="phoneNumber" value="${sessionScope.user.phoneNumber}">
                    </div>
                    <button type="submit" class="btn btn-success">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>

<!-- Địa chỉ thanh toán -->
<div class="card mb-4 shadow-sm">
    <div class="card-body">
        <h5 class="mb-3">Địa chỉ thanh toán: ${sessionScope.user.address}</h5>
        <form action="${pageContext.request.contextPath}/secured/user/profile?action=updateAddress" method="post" class="row g-3" id="addressForm">
            <div class="col-md-4">
                <label class="form-label">Tỉnh/Thành phố</label>
                <select class="form-select" id="province" name="province" required>
                    <option value="">Chọn Tỉnh/Thành phố</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Quận/Huyện</label>
                <select class="form-select" id="district" name="district" required>
                    <option value="">Chọn Quận/Huyện</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Phường/Xã</label>
                <select class="form-select" id="ward" name="ward" required>
                    <option value="">Chọn Phường/Xã</option>
                </select>
            </div>
            <div class="col-md-12">
                <label class="form-label">Địa chỉ chi tiết</label>
                <input type="text" class="form-control" name="address" placeholder="Số nhà, tên đường..." required>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-success">Lưu thay đổi</button>
            </div>
        </form>
    </div>
</div>

    <!-- Thay đổi mật khẩu -->
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="mb-3">Thay đổi mật khẩu</h5>
            <form action="${pageContext.request.contextPath}/secured/user/profile?action=updatePassword" method="post" class="row g-3">
                <div class="col-md-12">
                    <label class="form-label">Mật khẩu hiện tại</label>
                    <input type="password" class="form-control" name="nowPass">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Mật khẩu mới</label>
                    <input type="password" class="form-control" name="newPass">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Xác nhận mật khẩu</label>
                    <input type="password" class="form-control" name="confirmNewPass">
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-success">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    const provinceSelect = document.getElementById('province');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    // 1. Load tất cả Tỉnh/Thành phố
    fetch('https://provinces.open-api.vn/api/p/')
        .then(res => res.json())
        .then(provinces => {
            provinces.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.name;
                opt.textContent = p.name;
                provinceSelect.appendChild(opt);
            });
        });
        
        fetch(`https://provinces.open-api.vn/api/d/`)
            .then(res => res.json())
            .then(data => {
                const districts = data;
                districts.forEach(d => {
                    const opt = document.createElement('option');
                    opt.value = d.name;
                    opt.textContent = d.name;
                    districtSelect.appendChild(opt);
                });
            });
// Gọi API lấy danh sách Phường/Xã theo quận
        fetch(`https://provinces.open-api.vn/api/w/`)
            .then(res => res.json())
            .then(data => {
                const wards = data;
                wards.forEach(w => {
                    const opt = document.createElement('option');
                    opt.value = w.name;
                    opt.textContent = w.name;
                    wardSelect.appendChild(opt);
                });
            });
    

    
</script>


<jsp:include page="/footer.jsp" />
