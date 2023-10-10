const loginForm = document.getElementById('login-form');
const loginButton = document.getElementById('login-button');
const button = document.querySelector(".button");
const button2 = document.getElementById('signup')

loginForm.addEventListener('submit', (event) => {
  event.preventDefault();
  
  // Disable login button and show loading spinner
  loginButton.setAttribute('disabled', '');
  loginButton.innerHTML = '<span>Loading...</span>';

  // Get username and password from form inputs
  const username = document.querySelector('input[type=text]').value;
  const password = document.querySelector('input[type=password]').value;
 console.log(username);
 console.log(password);
  // Make API call to login endpoint
  function setCookie(cname, cvalue, exdays) {
    var d = new Date();
     d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
     var expires = "expires="+d.toUTCString();
     document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }
  fetch('http://localhost:8080/login/'+username+'/'+password)
  .then(response => response.json())
  .then(data => {
    const integerData = data;
    console.log(integerData);
    if(integerData ==1){
    alert("invalid username or password");
  }

  if(integerData ==0){
    //alert("Login Sucess you will be redirected to home page");
    setCookie("username",username,1);
    loginButton.removeAttribute('disabled');
    loginButton.innerHTML = 'Log In';
    window.location.href = "index.html";
   // window.location.href = "https://ayushcenter.xyz/";
   // window.location.href = "https://ayushcenter.xyz/";
    //setCookie("username",value1,1);
  }

  if(integerData ==2){
    alert("SQL error");
  }
  })
  
  .catch(error => console.error(error));




});

button.addEventListener('click', (event) => {
    event.preventDefault();
  
    // Disable login button and show loading spinner
    button.classList.add('button--loading');
    button.disabled = true;
    // Get username and password from form inputs
    const username = document.querySelector('input[type=text]').value;
    const password = document.querySelector('input[type=password]').value;
   console.log(username);
   console.log(password);
    // Make API call to login endpoint
    function setCookie(cname, cvalue, exdays) {
      var d = new Date();
       d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
       var expires = "expires="+d.toUTCString();
       document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
      }
    fetch('https://api.ayushcenter.xyz/login/'+username+'/'+password)
    .then(response => response.json())
    .then(data => {
      const integerData = data;
      console.log(integerData);
      if(integerData ==1){
      alert("invalid username or password");
      button.classList.remove('button--loading');
      button.disabled = false;
    }
  
    if(integerData ==0){
      //alert("Login Sucess you will be redirected to home page");
      setCookie("username",username,1);
      button.classList.remove('button--loading');
      button.disabled = false;
      window.location.href = "index.html";
     // window.location.href = "https://ayushcenter.xyz/";
     // window.location.href = "https://ayushcenter.xyz/";
      //setCookie("username",value1,1);
    }
  
    if(integerData ==2){
      alert("SQL error");
    }
    })
    
    .catch(error => console.error(error));
        
   
   });

   button2.addEventListener('click', (event) => {
   window.location.href = "signup.html";
   });

