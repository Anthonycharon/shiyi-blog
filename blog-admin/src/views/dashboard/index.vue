<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'
import editorDashboard from './editor'
import {getDashboard} from '@/api/home'
import { mapMutations } from "vuex";

export default {
  name: 'Dashboard',
  components: { adminDashboard, editorDashboard },
  data() {
    return {
      currentRole: 'adminDashboard',
    }
  },
  computed: {
    ...mapGetters([
      'role'
    ])
  },
}
</script>
