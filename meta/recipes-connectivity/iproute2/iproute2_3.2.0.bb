require iproute2.inc

PR = "r1"

#v3.2.0 tag is "447c118f138171b260ad045ad6e1b17f9ef462e2"
#but it was not fully tested and had build error, and the next commit fixed it.
SRCREV = "13603f6a9e46f08576f6284a0ef1ce1fbf94ffe0"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/shemminger/iproute2.git \
           file://configure-cross.patch"
S = "${WORKDIR}/git"

