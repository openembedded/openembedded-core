DESCRIPTION = "The MusicBrainz client is a library which can be built into other programs.  The library allows you to access the data held on the MusicBrainz server."
HOMEPAGE = "http://musicbrainz.org"
MAINTAINER = "Koen Kooi <koen@dominion.kabel.utwente.nl>"
LICENSE = "LGPL"
DEPENDS = "expat"

SRC_URI = "http://ftp.musicbrainz.org/pub/musicbrainz/libmusicbrainz-2.1.2.tar.gz"

inherit autotools pkgconfig

do_stage() {
autotools_stage_all
}



