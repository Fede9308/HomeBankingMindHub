Vue.createApp({
    data() {
        return {
            email: "",
            firstName: "",
            lastName: "",
            dni:"",
            outPut: "",
            clients: []
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        // load and display JSON sent by server for /clients
        loadData() {
            axios.get("/api/clients")
                .then((response) => {
                    // handle success
                    this.outPut = response.data;
                    this.clients = response.data._embedded.clients;
                })
                .catch((error) => {
                    alert("Error loading clients: " + error)
                })
        },
        // handler for when user clicks add client
        addClient() {
            if (this.email.length > 1 && this.firstName.length > 1 && this.lastName.length > 1 && this.dni.length > 1) {
                this.postClient(this.email, this.firstName, this.lastName, this.dni);
            }
        },
        // code to post a new client using AJAX
        // on success, reload and display the updated data from the server
        postClient(email, firstName, lastName, dni) {
            axios.post("/clients", { "email": email, "firstName": firstName, "lastName": lastName, "dni": dni })
                .then((response) => {
                    // handle success
                    this.loadData();
                    this.clearData();
                })
                .catch((error) => {
                    // handle error
                    alert("Error to create client: " + error)
                })
        },
        clearData() {
            this.firstName = "";
            this.lastName = "";
            this.email = "";
            this.dni = "";
        }
    }
}).mount("#app");