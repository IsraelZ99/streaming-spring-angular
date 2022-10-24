import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HttpClientService {
  constructor(private httpClient: HttpClient) {}

  public getVideo(name: string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        "responseType": "video/mp4"
      })
    }
    return this.httpClient.get(
      `http://127.0.0.1:8080/audiovideo/videos/byte/${name}.mp4`, httpOptions
    );
  }
}
