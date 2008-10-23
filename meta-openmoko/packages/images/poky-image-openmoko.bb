#
# Copyright (C) 2007 OpenedHand Ltd.
#

IMAGE_FEATURES += "apps-console-core ${X11_IMAGE_FEATURES} apps-x11-games"

inherit poky-image

IMAGE_INSTALL += "openmoko-today2 openmoko-dialer2 openmoko-theme-standard2 dates web-webkit eds-dbus tasks"
IMAGE_INSTALL += "openmoko-contacts2 openmoko-session2 \
    matchbox-keyboard \
    matchbox-stroke \
    matchbox-config-gtk \
    matchbox-themes-gtk \
    xcursor-transparent-theme \
    openmoko-icon-theme-standard \
    settings-daemon"
