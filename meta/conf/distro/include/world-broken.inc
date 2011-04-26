#
# Exclude known broken packages
#
EXCLUDE_FROM_WORLD_pn-gobject-introspection = "1"

# Need bzr
EXCLUDE_FROM_WORLD_pn-eds-tools = "1"

# Need Updates from Clutter 0.8
EXCLUDE_FROM_WORLD_pn-clutter-box2d = "1"

# Don't build older versions of clutter
EXCLUDE_FROM_WORLD_pn-clutter = "1"
EXCLUDE_FROM_WORLD_pn-clutter-1.4 = "1"
EXCLUDE_FROM_WORLD_pn-clutter-gst-1.4 = "1"
EXCLUDE_FROM_WORLD_pn-clutter-gtk-1.4 = "1"
