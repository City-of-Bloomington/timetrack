<s:if test="!isUserCurrentEmployee()">
  <header class="proxy">
</s:if>
<s:else>
  <header>
</s:else>
  <div class="container">
    <div class="brand">
      <a href="<s:property value='#application.url'/>">
        <img src="data:image/svg+xml,%3Csvg id='cob-logo' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 225 225'%3E%3Cpath id='shape' d='M91.071,0,112.5,21.429,133.929,0V42.857l21.428-21.428V64.286L133.929,85.714,112.5,64.286,91.071,85.714,69.643,64.286V21.429L91.071,42.857ZM50.3,35.412,36.729,21.838V36.991H21.576L35.15,50.564,21.429,64.286H64.286V21.429ZM0,133.929,21.429,112.5,0,91.071H42.857L21.429,69.643H64.286L85.714,91.071,64.286,112.5l21.428,21.429L64.286,155.357H21.429l21.428-21.428Zm64.286,26.785H21.429L35.412,174.7,21.838,188.272H36.99v15.152L50.564,189.85l13.722,13.721ZM133.929,225,112.5,203.571,91.071,225V182.143L69.643,203.571V160.714l21.428-21.428L112.5,160.714l21.429-21.428,21.428,21.428v42.857l-21.428-21.428Zm69.642-64.286H160.714v42.857L174.7,189.588l13.574,13.574V188.009h15.152L189.85,174.436ZM225,91.071,203.571,112.5,225,133.929H182.143l21.428,21.428H160.714l-21.428-21.428L160.714,112.5,139.286,91.071l21.428-21.428h42.857L182.143,91.071ZM189.588,50.3l13.574-13.574H188.009V21.576L174.436,35.15,160.714,21.429V64.286h42.857Z' transform='translate(0 0)' fill='%231e59ae'/%3E%3C/svg%3E">
        <div class="site-title">
          <h1>Time Track</h1>
          <h2>City of Bloomington, John Hamilton, Mayor</h2>
        </div>
      </a>
    </div>

    <div class="dropdown-wrapper">
      <s:if test="#session != null && #session.user != null">
        <nav class="dropdown">
          <button id="genericMenuLauncher"
                  class="launcher"
                  aria-haspopup="true"
                  aria-expanded="false">
            <s:if test="isUserCurrentEmployee()">
              <s:property value='#session.user.full_name' />
            </s:if>
            <s:if test="!isUserCurrentEmployee()">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class="user-proxy-icon"><path d="M383.9 308.3l23.9-62.6c4-10.5-3.7-21.7-15-21.7h-58.5c11-18.9 17.8-40.6 17.8-64v-.3c39.2-7.8 64-19.1 64-31.7 0-13.3-27.3-25.1-70.1-33-9.2-32.8-27-65.8-40.6-82.8-9.5-11.9-25.9-15.6-39.5-8.8l-27.6 13.8c-9 4.5-19.6 4.5-28.6 0L182.1 3.4c-13.6-6.8-30-3.1-39.5 8.8-13.5 17-31.4 50-40.6 82.8-42.7 7.9-70 19.7-70 33 0 12.6 24.8 23.9 64 31.7v.3c0 23.4 6.8 45.1 17.8 64H56.3c-11.5 0-19.2 11.7-14.7 22.3l25.8 60.2C27.3 329.8 0 372.7 0 422.4v44.8C0 491.9 20.1 512 44.8 512h358.4c24.7 0 44.8-20.1 44.8-44.8v-44.8c0-48.4-25.8-90.4-64.1-114.1zM176 480l-41.6-192 49.6 32 24 40-32 120zm96 0l-32-120 24-40 49.6-32L272 480zm41.7-298.5c-3.9 11.9-7 24.6-16.5 33.4-10.1 9.3-48 22.4-64-25-2.8-8.4-15.4-8.4-18.3 0-17 50.2-56 32.4-64 25-9.5-8.8-12.7-21.5-16.5-33.4-.8-2.5-6.3-5.7-6.3-5.8v-10.8c28.3 3.6 61 5.8 96 5.8s67.7-2.1 96-5.8v10.8c-.1.1-5.6 3.2-6.4 5.8z"/></svg>
              <s:property value="employee" />
            </s:if>
          </button>

          <div class="links" aria-labeledby="genericMenuLauncher" hidden>
            <s:if test="!isUserCurrentEmployee()">
              <div class="user-proxy">
                <b>Proxy User:</b><br><s:property value="employee" />
              </div>
              <a href="<s:property value='#application.url' />timeDetails.action?employee_id=<s:property value='user.id' />&action=Change">Stop User Proxy</a>
              <hr>
            </s:if>

            <s:if test="#session.user.isAdmin()">
              <a href="<s:property value='#application.url'/>settings.action">Settings</a>
            </s:if>
            <a href="<s:property value='#application.url'/>Logout">Logout</a>
          </div>
        </nav>
      </s:if>
    </div>
  </div>
</header>
