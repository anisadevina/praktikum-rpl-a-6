<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Daftar — Study Scope</title>
  <link rel="stylesheet" href="{{ asset('style/global.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/register.css') }}" />
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
        <h1>Daftar Sekarang</h1>
        <p>Lalu Akses Mata Kuliah dan Forum di Study Scope</p>
      </div>

      <!-- Form -->
      <form action="{{ route('register.proses') }}" method="POST" class="auth-form" id="registerForm" novalidate>
        @csrf
        <div class="form-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            name="username"
            placeholder="Masukkan Username"
            autocomplete="username"
            value="{{ old('username') }}"
            required
          />
<<<<<<< Updated upstream
          <span class="field-error" id="usernameError">@error('username') {{ $message }} @enderror</span>
=======
          <span class="field-error" id="usernameError"></span>
>>>>>>> Stashed changes
        </div>

        <div class="form-group">
          <label for="nim">NIM</label>
          <input
            type="text"
            id="nim"
            name="nim"
            placeholder="Masukkan NIM"
            value="{{ old('nim') }}"
            required
          />
<<<<<<< Updated upstream
          <span class="field-error" id="nimError">@error('nim') {{ $message }} @enderror</span>
=======
          <span class="field-error" id="nimError"></span>
>>>>>>> Stashed changes
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            name="email_user"
            placeholder="Masukkan email SSO"
            autocomplete="email"
            value="{{ old('email_user') }}"
            required
          />
<<<<<<< Updated upstream
          <span class="field-error" id="emailError">@error('email_user') {{ $message }} @enderror</span>
=======
          <span class="field-error" id="emailError"></span>
>>>>>>> Stashed changes
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <div class="input-password-wrapper">
            <input
              type="password"
              id="password"
              name="password"
              placeholder="••••••••"
              autocomplete="new-password"
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
<<<<<<< Updated upstream
          <span class="field-error" id="passwordError">@error('password') {{ $message }} @enderror</span>
=======
          <span class="field-error" id="passwordError"></span>
>>>>>>> Stashed changes
        </div>

        <span class="form-error" id="formError"></span>

        <div class="auth-actions">
          <button type="submit" class="btn btn-primary">Daftar</button>
        </div>

        <p class="auth-redirect">
          Sudah punya akun? <a href="{{ route('login') }}">Masuk di sini</a>
        </p>

      </form>
    </div>
  </div>

  <script src="{{ asset('script/register.js') }}"></script>
</body>
</html>