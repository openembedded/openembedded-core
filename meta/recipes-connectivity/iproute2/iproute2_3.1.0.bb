require iproute2.inc

#v3.1.0 tag
SRCREV = "9cbe6bc337a35b91882f92599eefeb161f3e776e"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/shemminger/iproute2.git \
           file://configure-cross.patch"
S = "${WORKDIR}/git"

