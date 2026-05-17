<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Login — Study Scope</title>
  <link rel="stylesheet" href="../../styles/global.css" />
  <link rel="stylesheet" href="login.css" />
</head>
<body>
  <div class="auth-wrapper">
    <div class="auth-card">

      <!-- Logo -->
      <div class="auth-logo">
        <div class="logo-placeholder">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"
                  stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span class="logo-text">Study Scope</span>
        </div>
      </div>

      <!-- Heading -->
      <div class="auth-heading">
        <h1>Selamat Datang di<br />Study Scope</h1>
      </div>

      <!-- Form -->
      <form class="auth-form" id="loginForm" novalidate>

        <div class="form-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            name="username"
            placeholder="Masukkan Username"
            autocomplete="username"
            required
          />
          <span class="field-error" id="usernameError"></span>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <div class="input-password-wrapper">
            <input
              type="password"
              id="password"
              name="password"
              placeholder="••••••••"
              autocomplete="current-password"
              required
            />
            <button type="button" class="toggle-password" aria-label="Tampilkan password">
              <svg id="eyeIcon" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
                fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
            </button>
          </div>
          <span class="field-error" id="passwordError"></span>
        </div>

        <span class="form-error" id="formError"></span>

        <div class="auth-actions">
          <button type="submit" class="btn btn-primary">Masuk</button>
          <a href="register.html" class="btn btn-secondary">Daftar</a>
        </div>

      </form>
    </div>
  </div>

  <script src="login.js"></script>
</body>
</html>