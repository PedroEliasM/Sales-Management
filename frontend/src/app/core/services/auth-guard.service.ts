import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = localStorage.getItem('token');
    const userType = localStorage.getItem('userType');
    const requiredRole = route.data['roles'] as Array<string>;

    if (token && userType) {
      if (!requiredRole || requiredRole.includes(userType.toLowerCase())) {
        return true;
      } else {
        this.router.navigate(['/unauthorized']); // Redirecionar para página de não autorizado
        return false;
      }
    } else {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }
  }
}