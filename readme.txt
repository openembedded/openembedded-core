Building An Image
===

Simply run;

source setdevenv
bitbake oh-image

will result in a jffs2 image for the c7x0 series.

To build for the 770 change to 

MACHINE = "nokia770"

in build/conf/local.conf

Notes:
===

o Useful Links;

  OE Homepage and wiki
  http://openembedded.org
 
  Koens PDA page has some nice tips;
  http://dominion.kabel.utwente.nl/koen/pda/?page=software&sub=bb