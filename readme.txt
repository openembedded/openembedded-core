Building An Image
===

Simply run;

source poky-init-build-env
bitbake oh-image-pda

will result in a jffs2 image for the c7x0 series.

To build for the 770 change to 

MACHINE = "nokia770"

in build/conf/local.conf

NOTE: The above commands must be run in the build directory. Running 
them anywhere else will cause confusion.

Notes:
===

o Useful Links;

  OE Homepage and wiki
  http://openembedded.org
 
  Koens PDA page has some nice tips;
  http://dominion.kabel.utwente.nl/koen/pda/?page=software&sub=bb
