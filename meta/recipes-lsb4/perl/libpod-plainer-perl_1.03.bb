SUMMARY = "Perl extension for converting Pod to old-style Pod"
DESCRIPTION = "Pod::Plainer uses Pod::Parser which takes Pod with the (new) 'C<< .. >>' \
constructs and returns the old(er) style with just 'C<>'; '<' and '>' are replaced by \
'E<lt>' and 'E<gt>'. \
\
This can be used to pre-process Pod before using tools which do not recognise the new style Pods."

HOMEPAGE = "http://search.cpan.org/dist/Pod-Plainer/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"

LIC_FILES_CHKSUM = "file://README;beginline=27;md5=80b4a99d477135bfcd1d0a44a041c63c"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/R/RM/RMBARKER/Pod-Plainer-${PV}.tar.gz"

SRC_URI[md5sum] = "15d42071d6bd861cb72daa8cc3111cd3"
SRC_URI[sha256sum] = "9d153b1d8609606a3424f07a7f4ce955af32131d484cb9602812122bb1ee745b"

S = "${WORKDIR}/Pod-Plainer-${PV}"

inherit cpan

BBCLASSEXTEND = "native"
