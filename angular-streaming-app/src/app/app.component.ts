import { Component } from '@angular/core';
import { HttpClientService } from './services/http-client.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'angular-streaming-app';

  public constructor(private httpClient: HttpClientService) {}
}
