DESCRIPTION = "Custom MB session files for poky"
LICENSE = "GPL"
SECTION = "x11"
RDEPENDS = "formfactor gtk-engines initscripts matchbox-session"
PR = "r15"

# This package is architecture specific because the session script is modified
# based on the machine architecture.
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://session"
S = "${WORKDIR}"

do_install() {
	install -d ${D}/${sysconfdir}/matchbox
	install ${S}/session ${D}/${sysconfdir}/matchbox/session
        chmod +x ${D}/${sysconfdir}/matchbox/session
}

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

# Theme
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/metacity/general/theme "Moblin-Netbook"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/metacity/general/button_layout ":close"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/gtk_theme "Moblin-Netbook"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/icon_theme "moblin"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/peripherals/mouse/cursor_theme "moblin"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/toolbar_style "icons"

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type bool -s \
            /desktop/gnome/interface/menus_have_icons false

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type bool -s \
            /desktop/gnome/interface/buttons_have_icons false

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/sound/theme_name moblin

# Window Actions
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/metacity/general/action_double_click_titlebar "none"
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/metacity/general/action_middle_click_titlebar "none"
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/metacity/general/action_right_click_titlebar "none"

# UX Shell
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type bool -s \
            /apps/metacity/general/clutter_disabled false

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type list --list-type string -s \
            /apps/metacity/general/clutter_plugins '[moblin-netbook]'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/url-handlers/http/command 'moblin-web-browser %s'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/url-handlers/https/command 'moblin-web-browser %s'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/background/picture_filename \
            '/usr/share/mutter-moblin/theme/myzone/toys.jpg'

# Fonts
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/document_font_name \
            'Liberation Sans 10'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/font_name \
            'Liberation Sans 10'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /desktop/gnome/interface/monospace_font_name \
            'Liberation Mono 10'

# Nautilus
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type bool -s \
            /apps/nautilus/preferences/always_use_browser true

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type bool -s \
            /apps/nautilus/icon_view/labels_beside_icons true

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/nautilus/icon_view/default_zoom_level large

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults \
            --direct --type string -s \
            /apps/gnome_settings_daemon/keybindings/power ""

#
# The interesting bit - we convert to run the system as the user "pokyuser"
# and setup this user account as needed
#
if [ ! -d /home/pokyuser ]; then
    adduser --disabled-password --ingroup users pokyuser

    # Setup any .skel files
    if [ -d ${sysconfdir}/skel ]; then
        cp -pPR ${sysconfdir}/skel/.[a-zA-Z0-9]* /home/pokyuser/ || /bin/true
        cp -pPR ${sysconfdir}/skel/* /home/pokyuser/ || /bin/true
    fi

    # Move any sample media into this user
    if [ -d ${sysconfdir}/skel-media ]; then
        mv ${sysconfdir}/skel-media/* /home/pokyuser/ || /bin/true
    fi

    chown pokyuser.users -R /home/pokyuser/* || /bin/true
    chown pokyuser.users -R /home/pokyuser/.[a-zA-Z0-9]* || /bin/true

    # Make sure Xorg is suid
    chmod a+s /usr/bin/Xorg

    # Tell X to run as this user
    mkdir -p ${sysconfdir}/X11/
    echo "pokyuser" > ${sysconfdir}/X11/Xusername

    # Add pokyuser to the audio group
    audiousers=`grep ^audio < /etc/group | cut -d ':' -f 4`
    if [ "x$audiousers" == "x" ]; then
        audiousers=pokyuser
    else
        audiousers=$audiousers,pokyuser
    fi
    sed -i -e "s/audio:\(.*\):\(.*\):\(.*\)/audio:\1:\2:$audiousers/" /etc/group
fi

}
