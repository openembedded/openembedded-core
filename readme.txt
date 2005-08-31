run;

source setdevenv
bitbake oh-image

will result in a jffs2 image for the c7x0 series.

To build for the 770 change to 

MACHINE = "nokia770"

in build/conf/local.conf
