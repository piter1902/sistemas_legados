<template>
  <div id="app">
    <p class="text-monospace">Número de registros : {{ numRegistros.num }}</p>
    <div class="btn-toolbar mb-3" role="toolbar" aria-label="Toolbar with button groups">
       <div class="btn-group" role="group" aria-label="Basic example">
        <button type="button" class="btn btn-secondary">Buscar por nombre</button>
        <button type="button" class="btn btn-secondary">Buscar por cinta</button>
      </div>
      <div class="input-group mx-auto" style="width: 200px;">
        <div class="input-group-prepend">
          <div class="input-group-text" id="btnGroupAddon">@</div>
        </div>
        <input type="text" class="form-control" placeholder="..." aria-label="Input group example" aria-describedby="btnGroupAddon">
      </div>
    </div>
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
          <tr>
            <th scope="row">Mario Bros</th>
            <td>Arcade</td>
            <td>45</td>
            <td>666</td>
          </tr>
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
      numRegistros: {
        num: 0
      }
    };
  },
  created() {
    // Llamada para obtener el número de registros al incio
    this.$http.get("http://localhost:8080/getRecords").then(
      function(response){
        this.numRegistros.num = response.body.numRegistros
    });
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
