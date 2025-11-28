
```
medocs-deploy
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ Dockerfile
├─ frontend
│  ├─ README.md
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ postcss.config.js
│  ├─ public
│  │  ├─ assets
│  │  │  ├─ Frame 121075764.svg
│  │  │  └─ Framelogo.svg
│  │  ├─ favicon.ico
│  │  ├─ index.html
│  │  ├─ logo.jpg
│  │  ├─ logo192.png
│  │  ├─ logo512.png
│  │  ├─ manifest.json
│  │  └─ robots.txt
│  ├─ src
│  │  ├─ App.css
│  │  ├─ App.js
│  │  ├─ App.test.js
│  │  ├─ components
│  │  │  └─ LoginForm.js
│  │  ├─ index.css
│  │  ├─ index.js
│  │  ├─ logo.svg
│  │  ├─ reportWebVitals.js
│  │  └─ setupTests.js
│  └─ tailwind.config.js
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ postcss.config.js
├─ readme.md
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ medoc
│  │  │        ├─ ControllerHelper.java
│  │  │        ├─ DataInitializer.java
│  │  │        ├─ FileUploadUtil.java
│  │  │        ├─ MainController.java
│  │  │        ├─ MedocApplication.java
│  │  │        ├─ MvcConfig.java
│  │  │        ├─ Utility.java
│  │  │        ├─ entity
│  │  │        │  ├─ Ordo.java
│  │  │        │  ├─ OrdoImage.java
│  │  │        │  ├─ Question.java
│  │  │        │  ├─ Role.java
│  │  │        │  ├─ Setting.java
│  │  │        │  ├─ SettingBag.java
│  │  │        │  ├─ SettingCategory.java
│  │  │        │  └─ User.java
│  │  │        ├─ ordo
│  │  │        │  ├─ OrdoController.java
│  │  │        │  ├─ OrdoNotFoundException.java
│  │  │        │  ├─ OrdoRepository.java
│  │  │        │  └─ OrdoService.java
│  │  │        ├─ question
│  │  │        │  ├─ QuestionController.java
│  │  │        │  ├─ QuestionNotFoundException.java
│  │  │        │  ├─ QuestionRepository.java
│  │  │        │  ├─ QuestionRestController.java
│  │  │        │  └─ QuestionService.java
│  │  │        ├─ report
│  │  │        │  ├─ ReportController.java
│  │  │        │  ├─ ReportExcelExporter.java
│  │  │        │  └─ ReportPDFExporter.java
│  │  │        ├─ security
│  │  │        │  ├─ MedocUserDetails.java
│  │  │        │  ├─ MedocUserDetailsService.java
│  │  │        │  └─ WebSecurityConfig.java
│  │  │        ├─ setting
│  │  │        │  ├─ EmailSettingBag.java
│  │  │        │  ├─ GeneralSettingBag.java
│  │  │        │  ├─ SettingController.java
│  │  │        │  ├─ SettingRepository.java
│  │  │        │  └─ SettingService.java
│  │  │        └─ user
│  │  │           ├─ RoleRepository.java
│  │  │           ├─ UserNotFoundException.java
│  │  │           ├─ UserRepository.java
│  │  │           ├─ UserService.java
│  │  │           ├─ controller
│  │  │           │  ├─ AccountController.java
│  │  │           │  ├─ ForgotPasswordController.java
│  │  │           │  ├─ UserController.java
│  │  │           │  └─ UserRestController.java
│  │  │           └─ export
│  │  │              ├─ AbstractExporter.java
│  │  │              ├─ UserCsvExporter.java
│  │  │              ├─ UserExcelExporter.java
│  │  │              └─ UserPdfExporter.java
│  │  └─ resources
│  │     ├─ application.properties
│  │     ├─ static
│  │     │  ├─ css
│  │     │  │  ├─ account.css
│  │     │  │  ├─ forgot_pass.css
│  │     │  │  ├─ index.css
│  │     │  │  ├─ login.css
│  │     │  │  ├─ navigation-login.css
│  │     │  │  ├─ navigation.css
│  │     │  │  ├─ order-detail.css
│  │     │  │  ├─ orders.css
│  │     │  │  ├─ ordonnance-form.css
│  │     │  │  ├─ ordonnances-list.css
│  │     │  │  ├─ register-form.css
│  │     │  │  ├─ report.css
│  │     │  │  ├─ settings.css
│  │     │  │  ├─ tailwind.css
│  │     │  │  ├─ user-form.css
│  │     │  │  └─ users-list.css
│  │     │  ├─ fontawesome
│  │     │  │  └─ all.css
│  │     │  ├─ images
│  │     │  │  ├─ background.jpg
│  │     │  │  ├─ default-user.png
│  │     │  │  ├─ icon1jpg.jpg
│  │     │  │  ├─ icon2.jpg
│  │     │  │  ├─ icon3.jpg
│  │     │  │  ├─ image-thumbnail.png
│  │     │  │  ├─ logo.jpg
│  │     │  │  └─ logo.png
│  │     │  ├─ js
│  │     │  │  ├─ common.js
│  │     │  │  ├─ common_form.js
│  │     │  │  ├─ common_list.js
│  │     │  │  ├─ common_modal.js
│  │     │  │  ├─ ordo_form.js
│  │     │  │  ├─ questionAnswer_post.js
│  │     │  │  └─ question_post.js
│  │     │  ├─ richtext
│  │     │  │  ├─ jquery.richtext.js
│  │     │  │  ├─ jquery.richtext.min.js
│  │     │  │  ├─ richtext.min.css
│  │     │  │  └─ richtext.scss
│  │     │  ├─ style.css
│  │     │  └─ webfonts
│  │     │     ├─ fa-brands-400.ttf
│  │     │     ├─ fa-brands-400.woff2
│  │     │     ├─ fa-regular-400.ttf
│  │     │     ├─ fa-regular-400.woff2
│  │     │     ├─ fa-solid-900.ttf
│  │     │     ├─ fa-solid-900.woff2
│  │     │     ├─ fa-v4compatibility.ttf
│  │     │     └─ fa-v4compatibility.woff2
│  │     └─ templates
│  │        ├─ error
│  │        │  ├─ 403.html
│  │        │  ├─ 404.html
│  │        │  └─ 500.html
│  │        ├─ error.html
│  │        ├─ fragments.html
│  │        ├─ index.html
│  │        ├─ login.html
│  │        ├─ message.html
│  │        ├─ modal_fragments.html
│  │        ├─ navigation.html
│  │        ├─ navigation_login.html
│  │        ├─ ordonnances
│  │        │  ├─ images_carousel.html
│  │        │  ├─ orders.html
│  │        │  ├─ ordo_detail.html
│  │        │  ├─ ordo_detail_modal.html
│  │        │  ├─ ordo_questions.html
│  │        │  ├─ ordonnance_form.html
│  │        │  ├─ ordonnance_images.html
│  │        │  ├─ ordonnance_pharma.html
│  │        │  ├─ ordonnances_list.html
│  │        │  └─ question_answer.html
│  │        ├─ questions_votes.html
│  │        ├─ register
│  │        │  ├─ register_form.html
│  │        │  ├─ register_success.html
│  │        │  ├─ verify_fail.html
│  │        │  └─ verify_success.html
│  │        ├─ report
│  │        │  └─ report.html
│  │        ├─ settings
│  │        │  ├─ mail_server.html
│  │        │  ├─ mail_templates
│  │        │  │  ├─ customer_verification.html
│  │        │  │  ├─ mail_templates.html
│  │        │  │  ├─ order_confirmation.html
│  │        │  │  ├─ order_confirmation_client.html
│  │        │  │  └─ order_reception_pharma.html
│  │        │  └─ settings.html
│  │        └─ users
│  │           ├─ account_form.html
│  │           ├─ forgot_password_form.html
│  │           ├─ reset_password_form.html
│  │           ├─ user_form.html
│  │           └─ users.html
│  └─ test
│     └─ java
│        └─ com
│           └─ medoc
│              ├─ ordo
│              │  └─ OrdoRepositoryTests.java
│              ├─ setting
│              │  └─ SettingRepositoryTests.java
│              └─ user
│                 ├─ RoleRepositoryTests.java
│                 └─ UserRepositoryTests.java
├─ tailwind.config.js
└─ user-photos
   ├─ 1
   │  └─ WhatsApp-Image-2024-07-26-at-2.33.47-PM-1536x1536.jpeg
   ├─ 10
   │  └─ WhatsApp Image 2024-07-24 à 19.13.57_5e1c559e.jpg
   ├─ 13.jpeg
   ├─ 19.jpeg
   ├─ 2
   │  └─ food .png
   ├─ 3
   │  └─ mobileV.PNG
   ├─ 3.jpeg
   ├─ 4
   │  └─ WhatsApp Image 2024-07-24 à 19.13.57_5e1c559e.jpg
   ├─ 4.jpg
   └─ 5
      └─ WhatsApp Image 2024-07-29 à 11.24.36_4e55a2bc.jpg

```