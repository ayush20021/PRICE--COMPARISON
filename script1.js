const button = document.querySelector(".button");;

function setCookie(cname, cvalue, exdays) {
var d = new Date();
 d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
 var expires = "expires="+d.toUTCString();
 document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}



button.addEventListener('click', () => {

    button.classList.add('button--loading');
    const username = document.querySelector('input[type=text]').value;
    const password = document.querySelector('input[type=password]').value;
    const mail = document.querySelector('input[type=mail]').value;
  console.log(username);
  console.log(password);
    console.log(mail);
    fetch('https://api.ayushcenter.xyz/add/'+username+'/'+password+'/'+mail)
      .then(response => response.json())
      .then(data => {
        const integerData = data;
        console.log(integerData);
        if(integerData ==1){
        alert("User already exists");
        button.classList.remove('loading');
        button.disabled = false;
      }

      if(integerData ==0){
        //alert("Login Sucess you will be redirected to home page");
       // window.location.href = "in.htm";
       window.location.href = "index.html";
       setCookie("username",value1,1);
        button.classList.remove('button--loading');
        button.disabled = false;
      }

      if(integerData ==2){
        alert("SQL error");
        button.classList.remove('loading');
        button.disabled = false;
      }
      })
      
      .catch(error => console.error(error));

      

  });