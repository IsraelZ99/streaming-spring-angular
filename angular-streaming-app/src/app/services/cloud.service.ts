import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CloudService {
  files: any = [
    {
      url: 'http://127.0.0.1:8080/audiovideo/audios/audio1.mp3',
      name: 'Horror',
      artist: 'Dross',
    },
    {
      url: 'https://s3-us-west-2.amazonaws.com/anchor-audio-bank/staging/2020-03-05/6295d331c4f0a5a77c54c391ee76aabf.m4a',
      name: "You will be successfull. If you ask yourself 'Why'",
      artist: 'Shibaji Debnath',
    },
    {
      url: 'https://s3-us-west-2.amazonaws.com/anchor-audio-bank/staging/2020-02-03/648e6a1cf78f0005ab9b127bd81e6bfc.m4a',
      name: "Build your career as you think. Question youself 'How'",
      artist: 'Shibaji Debnath',
    },
  ];

  constructor() {}

  public getFiles() {
    return of(this.files);
  }
}
