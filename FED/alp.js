
      function checkCookie() {
        var username = getCookie("username");
        if (username != "") {
          console.log("Welcome again ");
          // The cookie is present, render the HTML page
          document.getElementById("welcome").innerHTML = "Welcome " + username;
          console.log("Welcome " + username);
        } else {
          // The cookie is not present, redirect to the login page
          // alert("You are not logged in. You will be redirected to the login page.");
          window.location.href = "/Login";
          // window.location.href = "https://ayushcenter.xyz/login";
          console.log("You are not logged in. You will be redirected to the login page.");
        }
      }

      function getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(";");
        for (var i = 0; i < ca.length; i++) {
          var c = ca[i];
          while (c.charAt(0) == " ") {
            c = c.substring(1);
          }
          if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
          }
        }
        return "";
      }

      // Fetch data from API
      const username = getCookie("username");
      console.log("Welcome " + username);
      var p_dat = "l";
      fetch("http://localhost:8080/list/" + username)
        .then((response) => response.json()) // Parse response as JSON
        .then((data) => {
          // Populate dropdown menu with data
          const dropdown = document.getElementById("dropdownMenu");

          // Loop through the list items and create options in the dropdown
          data.forEach((item) => {
            const option = document.createElement("option");
            option.value = item.value;
            option.textContent = item.label;
            dropdown.appendChild(option);
          });

          // Add event listener for dropdown change
          dropdown.addEventListener("change", (event) => {
            const selectedItem = event.target.value;
            const selectedItemElement = document.getElementById("selectedItem");
            selectedItemElement.textContent = `Selected Item: ${selectedItem}`;
            p_dat = selectedItem;

            // Log selected item to console
            console.log(`Selected Item: ${selectedItem}`);
          });
        })
        .catch((error) => {
          console.error("Error retrieving list items:", error);
        });

      const button = document.querySelector(".butt");
      let myChart;
      button.addEventListener("click", () => {
        // Function to fetch data and create chart
        const fetchDataAndCreateChart = (username, p_dat) => {
          fetch("http://localhost:8080/data/" + username + "/" + p_dat)
            .then((response) => response.json())
            .then((data) => {
              // Check if a chart instance already exists
              if (typeof myChart !== "undefined") {
                // If a chart instance exists, destroy it
                myChart.destroy();
              }

              // JavaScript code to create the chart
              const ctx = document
                .getElementById("priceChart")
                .getContext("2d");
              const chart = new Chart(ctx, {
                type: "line",
                data: {
                  labels: data.labels,
                  datasets: data.datasets,
                },
                options: {
                  // ... your options here ...

                  
                },
              });

              // Assign the chart instance to the global variable for future reference
              myChart = chart;
            })
            .catch((error) => console.error("Error fetching data:", error));
        };

        // Call the function to fetch data and create chart
        fetchDataAndCreateChart(username, p_dat);
      });
    