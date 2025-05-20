#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import bb.utils
import os


def search_file(path, files, d):
    """
    Locate one of ``files`` in the list of paths ``path``.

    Arguments:

    -  ``path`` : list of colon-separated paths (like ``$PATH``).
    -  ``files``: list of file names to search (may be absolute paths).
    - ``d``     : Bitbake's data store.

    Returns the first file found, otherwise an empty string.

    This function also adds dependencies of the bitbake parse cache on all
    eligible paths so that the cache becomes invalid when one of these paths
    gets created (ie: the user adds a new overriding file).
    """
    for f in files:
        if os.path.isabs(f):
            bb.parse.mark_dependency(d, f)
            if os.path.exists(f):
                return f
        else:
            searched, attempts = bb.utils.which(path, f, history=True)
            for af in attempts:
                bb.parse.mark_dependency(d, af)
            if searched:
                return searched
