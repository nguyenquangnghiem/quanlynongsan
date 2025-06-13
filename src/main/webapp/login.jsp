<%-- 
    Document   : login
    Created on : 5 June 2025, 5:45:07 pm
    Author     : nghiem
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />
<style>
    .login{
        border-radius: 8px;
        border: 1px solid var(--Gray-Scale-Gray-50, #F2F2F2);
        background: var(--Gray-Scale-White, #FFF);
        box-shadow: 0px 0px 56px 0px rgba(0, 38, 3, 0.08);
        display: flex;
padding: 24px 24px 32px 24px;
flex-direction: column;
align-items: center;
gap: 20px;
width: 520px;
margin: 0 auto;
    }
    
    h1 {
        color: var(--Gray-Scale-Gray-900, #1A1A1A);

/* Heading 05/Heading 05 — 600 */
font-family: "Be Vietnam Pro";
font-size: 32px;
font-style: normal;
font-weight: 600;
line-height: 120%; /* 38.4px */
    }
    
    .email{
        margin-bottom: 12px;
    }
    
     .form-outline input.form-control {
    width: 472px;
    color: var(--Gray-Scale-Gray-400, #999);
font-family: Poppins;
font-size: 16px;
font-style: normal;
font-weight: 400;
line-height: 130%; /* 20.8px */
  }
  
  .inp{
      border-radius: 6px;
border: 1px solid var(--Gray-Scale-Gray-100, #E6E6E6);
background: var(--Gray-Scale-White, #FFF);
padding: 14px 16px;
  }
  
  .forget-pass{
      margin-top: 16px;
      margin-bottom: 20px;
      color: var(--Gray-Scale-Gray-600, #666);

/* Body Small/Body Small, 400 */
font-family: "Be Vietnam Pro";
font-size: 14px;
font-style: normal;
font-weight: 400;
line-height: 150%; /* 21px */
text-align: right;
display: block;
cursor: pointer;
  }
  
  .forget-pass:hover {
      text-decoration: underline;
  }
  .register{
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 24px;
      color: var(--Gray-Scale-Gray-600, #666);

/* Body Small/Body Small, 400 */
font-family: "Be Vietnam Pro";
font-size: 14px;
font-style: normal;
font-weight: 400;
line-height: 150%; /* 21px */
  }
  
  .register a{
      color: var(--Gray-Scale-Gray-900, #1A1A1A);

/* Body Small/Body Small, 500 */
font-family: "Be Vietnam Pro";
font-size: 14px;
font-style: normal;
font-weight: 500;
line-height: 150%; /* 21px */
cursor: pointer;
  }
  
  .register a:hover{
      text-decoration: underline;
  }
  
</style>
<div class="login">
        <h1>Đăng nhập</h1>
        <form action="login" method="POST">
<div class="form-outline email inp" data-mdb-input-init>
  <input type="text" name="email" id="typeText" class="form-control" />
  <label class="form-label" for="typeText">Email</label>
</div>
            <div class="form-outline inp" data-mdb-input-init>
  <input type="password" id="typeText" name="password" class="form-control" />
  <label class="form-label" for="typeText">Mật khẩu</label>
</div>
            <a class="forget-pass">Quên mật khẩu</a>
            <button type="submit" class="btn btn-success w-100" data-mdb-ripple-init>Đăng nhập</button>
            <p class="register">Bạn không có tài khoản?<a href="register">Đăng ký</a></p>
        </form>
</div>
<jsp:include page="/footer.jsp" />