import Cookies from 'js-cookie'

export function getToken() {
  let token = localStorage.getItem("token");
  if (token == null) token = sessionStorage.getItem("token")
  return token
}
export function setToken(res) {
 /* let inFifteenMinutes = new Date(new Date().getTime() + 60 * 60 * 1000);
  return Cookies.set("token", token,{expires:inFifteenMinutes})*/
  if (res.rememberMe){
    return localStorage.setItem("token", res.token)
  }else {
    return sessionStorage.setItem("token", res.token)
  }
}
export function removeToken() {
  localStorage.removeItem("token");
  sessionStorage.removeItem("token")
}

export function hasAuth(perms, perm) {
  let hasA = false
  perms.forEach(p => {
    if (p.indexOf(perm) !== -1) {
      hasA = true
      return false
    }
  })
  return hasA
}
