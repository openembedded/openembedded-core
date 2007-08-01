DESCRIPTION = "The MusicBrainz client is a library which can be built into other programs.  The library allows you to access the data held on the MusicBrainz server."
HOMEPAGE = "http://musicbrainz.org"
LICENSE = "LGPL"
DEPENDS = "expat"
PR = "r1"

SRC_URI = "http://ftp.musicbrainz.org/pub/musicbrainz/libmusicbrainz-2.1.2.tar.gz \
           file://change-len-type.patch;patch=1"

inherit autotools pkgconfig

do_stage() {
autotools_stage_all
}



