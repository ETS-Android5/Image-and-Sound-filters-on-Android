# Image and Sound filters on Android

Purpose of this project is educational in its base – it is to provide students a way to see how different image/sound filters work by allowing them to apply these filters on varying songs/photos/etc.

**RECOMMEDED ANDOIRD VERVION: 7.0**

## MAIN SCREEN

![obraz](https://user-images.githubusercontent.com/36985129/170359997-1d51480b-2c45-4a2c-8d5e-3e6b11324b3e.png)


    (1)  SOUND FILTERS button  - moves user to „Sound filtering” screen of the application.
    (2)  IMAGE FILTERS button – moves user to „Image filtering” screen of the application. After clicking this button gallery opens and user chooses image to be displayed in “Image filtering” screen


## SOUND FILTERING

![obraz](https://user-images.githubusercontent.com/36985129/170360069-e1a408f2-ecb9-4352-a427-6b8217e56858.png)


    (1) Song list
    (2) Audio player
    (3) Filter list

### Song list

In this section user can choose which song/sound should be loaded to the audio player. Clicking on one of the song names results in it being loaded to audio player. User can choose betwen 10 songs with precomputed effects. All used songs operate on Creative Commons Zero license meaning they can be downloaded, used, modified without any repercussions.


### Audio player

This section allows user to play chosen songs. It consists of:

    • “Play” button – starts the song from the moment indicated on the seek bar
    • “Pause” button – pauses the song
    • “Stop” button – stops the song
    • Seek bar – shows how much of the song has been played, uses can change position on the seek bar resulting the change of the moment from which song is/will be playing

### Filter list

In this section user can choose between 6 filters/effects which will be applied to the chosen song.
    • Time warping x2

Simplest method of changing the duration of a song. In this application time warping x2 has been used meaning song is sped up times 2. It this technique simply every other sample is being removed resulting in modification of both time domain and pitch of signal.

    • Time-scaling x2

More sophisticated method of changing the duration of a song. This technique minimizes/nullifies modification of pitch.

    • Pitch-scaling x2

Modification of pitch meaning shifting the fundamental frequency of song (ex. Pitch-scaling x2 moves the fundamental frequency from f0 to 2*f0 – now sound is 2 times higher) without altering duration of sound.

    • Low-pass filter 1kHz

Filter that leaves frequencies below 1 kHz intact and attenuates those above 1 kHz. Due lower frequencies being perceived better by human hearing system the output signal will seem as slightly more quiet than before filtering.

    • High-pass filter 1 kHz

Similar effect to the low-pass filtering only now lower frequencies are attenuated. Output signal seems much more quiet than before filtering.

    • Echo

Simulates echo (effect being result of signal bouncing of obstacles). “Echo signal”, in this case, is delayed 0.5 second and 2 times weaker than original signal.

## IMAGE FILTERING
    (1) Image display
    (2) Buttons section
    (3) Image filters list

### Image display

In this section images are displayed. Upon loading an image it’s resolution  may be downscaled to prevent errors.

Buttons section

    • Load – opens gallery and allows user to choose new image
    • Save – allows to save image after filters were used on it
    • Change – changes display between default image and filtered one to save changes better

### Image filters list

Similar to “Filter list” section in “SOUND FILTERING” user can choose from the list of 10 filters/effects to be used. Each filter uses 3x3 mask applied to each pixel of image.

Default image:

![obraz](https://user-images.githubusercontent.com/36985129/170360538-9b555f98-d778-4bf9-b284-a03423a08fdb.png)


- Low-pass filter

![obraz](https://user-images.githubusercontent.com/36985129/170360601-e009271a-285b-4fbc-b7bb-74876aeab493.png)


It passes low frequency elements and attenuates high frequency ones (like noises, little details). In the aftermath image becomes blurred and smother. In this application filter computes the mean of all pixels within the mask and assigns this value as new pixel for which computation was made.


- High-pass filter

![obraz](https://user-images.githubusercontent.com/36985129/170360643-c79636c7-86d2-40d2-a649-3eb378c2ff66.png)


It passes high frequency elements, like noises or details, and make them more distinct. Output image is much sharper than default one. In this application “mean removal” algorithm was used – the value of main pixel is amplified and its neighbors are attenuated.

- Moving filters
    
![obraz](https://user-images.githubusercontent.com/36985129/170360664-6b93ba25-605d-49ef-a2b6-5f1cedafe3c1.png)

They move pixels of image in chosen direction then preform subtraction between moved and original image. They are one of many examples of edge-detection filters. Vertical filter moves pixels left/right, horizontal up/down.

- Embossing filters
    
![obraz](https://user-images.githubusercontent.com/36985129/170360688-5937a3e0-131a-4b22-abc5-0b8e089440b9.png)

They make pixels seem more embossed and highlighted where edges are. South filter highlights edges leaning south (down) and east filter those leaning east (right).

- Sobel filter

![obraz](https://user-images.githubusercontent.com/36985129/170361133-718233d2-e161-4b88-8bcc-7828acec6622.png)

Sobel filter  also called “Sobel Edge Detector”. It is used for edge detection. In this application it highlights vertical edges.

- Warming filter

![obraz](https://user-images.githubusercontent.com/36985129/170361162-ebd4db43-afe1-4513-9917-be9dd0916fea.png)

Filter developed specifically for this application. It takes mask of edge-detection filter, but in case if value of one pixels within the mask gets too low it is nullified. Then the mean of all pixels is computed and this value is assigned to pixel on which mask was used. It makes image much brighter, colors like red or purple get some “glowing” effect



- Maximizing filter

![obraz](https://user-images.githubusercontent.com/36985129/170361209-292fd42a-d02e-4bd5-809f-db379c09ab2c.png)

Also called decompressing filter. From the pixels within the mask it takes one with the biggest value and assigns it to the pixel on which mask was used. It increases brightness and makes objects on image seem bigger.

- Minimizing filter

![obraz](https://user-images.githubusercontent.com/36985129/170361225-d806ab42-c252-4da9-9967-918f0af136ba.png)

Also called compressing or erosion filter. From the pixels within the mask it takes one with the smallest value and assigns it to the pixel on which mask was used. It reduces brightness and gives image “painted” look.
