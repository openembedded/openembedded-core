#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import errno
import re
import os


class OEList(list):
    """OpenEmbedded 'list' type

    Acts as an ordinary list, but is constructed from a string value and a
    separator (optional), and re-joins itself when converted to a string with
    str().  Set the variable type flag to 'list' to use this type, and the
    'separator' flag may be specified (defaulting to whitespace)."""

    name = "list"

    def __init__(self, value, separator = None):
        if value is not None:
            list.__init__(self, value.split(separator))
        else:
            list.__init__(self)

        if separator is None:
            self.separator = " "
        else:
            self.separator = separator

    def __str__(self):
        return self.separator.join(self)

def choice(value, choices):
    """OpenEmbedded 'choice' type

    Acts as a multiple choice for the user.  To use this, set the variable
    type flag to 'choice', and set the 'choices' flag to a space separated
    list of valid values."""
    if not isinstance(value, str):
        raise TypeError("choice accepts a string, not '%s'" % type(value))

    value = value.lower()
    choices = choices.lower()
    if value not in choices.split():
        raise ValueError("Invalid choice '%s'.  Valid choices: %s" %
                         (value, choices))
    return value

class NoMatch(object):
    """Stub python regex pattern object which never matches anything"""
    def findall(self, string, flags=0):
        return None

    def finditer(self, string, flags=0):
        return None

    def match(self, flags=0):
        return None

    def search(self, string, flags=0):
        return None

    def split(self, string, maxsplit=0):
        return None

    def sub(pattern, repl, string, count=0):
        return None

    def subn(pattern, repl, string, count=0):
        return None

NoMatch = NoMatch()

def regex(value, regexflags=None):
    """
    OpenEmbedded 'regex' type.

    Acts as a regular expression, returning the pre-compiled regular
    expression pattern object from the string ``value``.

    To use this type, set a BitBake variable ``type`` flag
    to ``regex``, and optionally, set the ``regexflags`` flag to a space-separated
    list of the flags to control the regular expression matching.

    The variable can then be used with :py:func:`oe.data.typed_value`.

    For example::

        FOO = "^foo[0-9]+$"
        FOO[type] = "regex"
        FOO[regexflags] += "ignorecase"

    Then:

    .. code-block:: python

       if re.match(oe.data.typed_value("FOO", d), somevalue):
           ...

    See the Python documentation on the :py:mod:`re` module for a list of
    valid flags.
    """

    flagval = 0
    if regexflags:
        for flag in regexflags.split():
            flag = flag.upper()
            try:
                flagval |= getattr(re, flag)
            except AttributeError:
                raise ValueError("Invalid regex flag '%s'" % flag)

    if not value:
        # Let's ensure that the default behavior for an undefined or empty
        # variable is to match nothing. If the user explicitly wants to match
        # anything, they can match '.*' instead.
        return NoMatch

    try:
        return re.compile(value, flagval)
    except re.error as exc:
        raise ValueError("Invalid regex value '%s': %s" %
                         (value, exc.args[0]))

def boolean(value):
    """
    OpenEmbedded 'boolean' type.

    To use this type, set a BitBake variable ``type`` flag
    to ``boolean``, and its value depending on the expected behavior:

    -  To return ``True``: ``yes``, ``y``, ``true``, ``t`` or ``1``.
    -  To return ``False``: ``no``, ``n``, ``false``, ``f``, ``0`` or ``None``.

    The variable can then be used with :py:func:`oe.data.typed_value`.

    For example::

        FOO = "yes"
        FOO[type] = "boolean"

    Then:

    .. code-block:: python

       if oe.data.typed_value("FOO", d):
           # condition is met as FOO = "yes" so becomes "True"
           ...
    """
    if value is None:
        return False

    if isinstance(value, bool):
        return value

    if not isinstance(value, str):
        raise TypeError("boolean accepts a string, not '%s'" % type(value))

    value = value.lower()
    if value in ('yes', 'y', 'true', 't', '1'):
        return True
    elif value in ('no', 'n', 'false', 'f', '0'):
        return False
    raise ValueError("Invalid boolean value '%s'" % value)

def integer(value, numberbase=10):
    """
    OpenEmbedded 'integer' type.

    To use this type, set a BitBake variable ``type`` flag
    to ``integer``, and its value to an integer string. Optionally, the
    ``numberbase`` flag can be set (defaults to base 10).

    The variable can then be used with :py:func:`oe.data.typed_value`.

    For example::

        FOO = "13"
        FOO[type] = "integer"

    Then:

    .. code-block:: python

       if oe.data.typed_value("FOO", d) > 12:
           # condition is met as FOO = "13" and 13 > 12
           ...
    """

    return int(value, int(numberbase))

_float = float
def float(value, fromhex='false'):
    """
    OpenEmbedded 'float' type.

    To use this type, set a BitBake variable ``type`` flag
    to ``float``, and its value to a floating number string. Optionally, the
    ``fromhex`` flag can be set to "true" to use Python's built-in
    :py:func:`float.fromhex` (defaults to "false").

    The variable can then be used with :py:func:`oe.data.typed_value`.

    For example::

        FOO = "1.23"
        FOO[type] = "float"

    Then:

    .. code-block:: python

       if oe.data.typed_value("FOO", d) > 1:
           # condition is met as FOO = "1.23" and 1.23 > 1
           ...
    """

    if boolean(fromhex):
        return _float.fromhex(value)
    else:
        return _float(value)

def path(value, relativeto='', normalize='true', mustexist='false'):
    """
    OpenEmbedded 'path' type.

    To use this type, set a BitBake variable ``type`` flag
    to ``path``, and its value to a path. Optionally, the following variable
    flags can be defined:

    -  ``relativeto``: another path string to join to the variable's value, with
       :py:func:`os.path.join`:

       .. code-block:: python

          os.path.join(relativeto, value)

    -  ``normalize``: a boolean string ("true" by default) that normalizes the
       path with :py:func:`os.path.normpath`.

    -  ``mustexist``: a boolean string ("false" by default) that ensures the
       path to the file/directory exists, otherwise raises
       :py:class:`ValueError`.

    The variable can then be used with :py:func:`oe.data.typed_value`.

    For example::

        FOO = "src/pyproject.toml"
        FOO[type] = "path"
        FOO[relativeto] = "${S}"

    Then:

    .. code-block:: python

       >>> print(oe.data.typed_value("FOO", d))
       ".../path/to/source/src/pyproject.toml"
    """
    value = os.path.join(relativeto, value)

    if boolean(normalize):
        value = os.path.normpath(value)

    if boolean(mustexist):
        try:
            with open(value, 'r'):
                pass
        except IOError as exc:
            if exc.errno == errno.ENOENT:
                raise ValueError("{0}: {1}".format(value, os.strerror(errno.ENOENT)))

    return value
