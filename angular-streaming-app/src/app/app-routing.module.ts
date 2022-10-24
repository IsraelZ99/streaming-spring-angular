import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VideoComponent } from './components/video/video.component';
import { AudioComponent } from './components/audio/audio.component';

const routes: Routes = [
  { path: '', redirectTo: '/audio', pathMatch: 'full' },
  { path: 'video', component: VideoComponent },
  { path: 'audio', component: AudioComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
