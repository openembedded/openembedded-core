export IMAGE_BASENAME = "bootstrap-image"
export IMAGE_LINGUAS = ""
export IPKG_INSTALL = "task-bootstrap"

DEPENDS = "task-bootstrap"

inherit image_ipk

FEED_URIS_append_openzaurus = " x11##http://openzaurus.org/official/unstable/${DISTRO_VERSION}/feed/x11 \
                                gpe##http://openzaurus.org/official/unstable/${DISTRO_VERSION}/feed/gpe \
                               opie##http://openzaurus.org/official/unstable/${DISTRO_VERSION}/feed/opie \
                                  e##http://openzaurus.org/official/unstable/${DISTRO_VERSION}/feed/e"

FEED_URIS_append_familiar   = " x11##http://familiar.handhelds.org/releases/${DISTRO_VERSION}/feed/x11 \
                               opie##http://familiar.handhelds.org/releases/${DISTRO_VERSION}/feed/opie"

LICENSE = MIT
