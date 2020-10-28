<template>
  <div id="app">
    <p class="text-monospace">Número de registros : {{ numRegistros.num }}</p>
    <div class="btn-toolbar mb-3 mx-auto" style="width: 200px" role="toolbar" aria-label="Toolbar with button groups">
      <div class="input-group" >
        <div class="input-group-prepend">
           <div class="btn-group" role="group">
            <button id="btnGroupDrop1" type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">🔎</button>
            <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
              <a class="dropdown-item" href="#" @click="filterByName(filter)" >Buscar por nombre</a>
              <a class="dropdown-item" href="#" @click="filterByTape(filter)">Buscar por cinta</a>
            </div>
          </div>
        </div>
        <input type="text" class="form-control" placeholder="..." aria-label="Input group example" aria-describedby="btnGroupAddon" v-model="filter">
      </div>
    </div>
    <br><br><br>
    <div class="container-fluid">
      <table class="table">
        <thead class="thead-dark">
          <tr>
            <th scope="col">Nombre</th>
            <th scope="col">Tipo</th>
            <th scope="col">Cinta</th>
            <th scope="col">Registro</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prog in this.filteredData" :key="prog.registry">
            <th scope="row">{{ prog.name }}</th>
            <td>{{ prog.type }}</td>
            <td>{{ prog.tape }}</td>
            <td>{{ prog.registry }}</td>
          </tr>
          <!-- <tr>
            <th scope="row">Mario Bros</th>
            <td>Arcade</td>
            <td>45</td>
            <td>666</td>
          </tr> -->
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {
  },
  data() {
    return  {
      filter: "",
      numRegistros: {
        num: 0
      },
      filteredData: []
    };
  },
  created() {
    // Llamada para obtener el número de registros al incio
    this.$http.get("http://localhost:8080/getRecords").then(
      function(response){
        this.numRegistros.num = response.body.numRegistros
    });
  },
  methods: {
    filterByName(name) {
      console.log("Filtering by name " + name)
      this.$http.get("http://localhost:8080/filterByName?name=" + name).then(
        function(response) {
          if(response.status == 200){
            // La operacion ha ido bien
            console.log("La operacion ha ido bien")
            this.filteredData = response.body
          }
        }
      );
    },
    filterByTape(tape) {
      console.log("Filtering by tape " + tape)
      this.$http.get("http://localhost:8080/filterByTape?tape=" + tape).then(
        function(response) {
          if(response.status == 200){
            // La operacion ha ido bien
            console.log("La operacion ha ido bien")
            this.filteredData = response.body
          }
        }
      );
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
