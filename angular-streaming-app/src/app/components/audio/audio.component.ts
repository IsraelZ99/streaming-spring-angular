import { Component, OnInit } from '@angular/core';
import { AudioService } from '../../services/audio.service';
import { CloudService } from '../../services/cloud.service';
import { StreamState } from '../../models/stream-state';

@Component({
  selector: 'app-audio',
  templateUrl: './audio.component.html',
  styleUrls: ['./audio.component.css'],
})
export class AudioComponent {
  public files: Array<any> = [];
  public state!: StreamState;
  public currentFile: any = {};

  public constructor(
    public audioService: AudioService,
    public cloudService: CloudService
  ) {
    // get media files
    cloudService.getFiles().subscribe((files) => (this.files = files));

    // listen to stream state
    this.audioService.getState().subscribe((state) => {
      this.state = state;
    });
  }

  public isFirstPlaying() {
    return this.currentFile.index === 0;
  }

  public isLastPlaying() {
    return this.currentFile.index === this.files.length - 1;
  }

  public onSliderChangeEnd(change: any) {
    this.audioService.seekTo(change);
  }

  public onVolumeChange(volume: any) {
    this.audioService.setVolume(volume.value);
  }

  public playStream(url: string) {
    this.audioService.playStream(url).subscribe((events: any) => {
      // listening for fun here
      // console.log(events);
      if (events.type === 'ended') {
        if (!this.isLastPlaying()) {
          this.next();
        } else {
          this.openFile(this.files[0], 0);
        }
      }
    });
  }

  public next() {
    const index = this.currentFile.index + 1;
    const file = this.files[index];
    this.openFile(file, index);
  }

  public openFile(file: any, index: number) {
    this.currentFile = { index, file };
    this.audioService.stop();
    this.playStream(file.url);
  }

  public pause() {
    this.audioService.pause();
  }

  public play() {
    this.openFile(this.files[0], 0);
    this.audioService.play();
  }

  public stop() {
    this.audioService.stop();
  }

  public previous() {
    const index = this.currentFile.index - 1;
    const file = this.files[index];
    this.openFile(file, index);
  }
}
