/**
 * Created by emunoz on 11/27/15.
 */
public class Item {
    private String _bankKey;
    private String _itemKey;
    private String _format;
    private String _responseType;
    private String _position;
    private String _filePath;

    public String get_filePath() { return _filePath; }

    public void set_filePath(String _filePath) { this._filePath = _filePath; }

    public String get_bankKey() {
        return _bankKey;
    }

    public void set_bankKey(String _bankKey) {
        this._bankKey = _bankKey;
    }

    public String get_itemKey() {
        return _itemKey;
    }

    public void set_itemKey(String _itemKey) {
        this._itemKey = _itemKey;
    }

    public String get_format() {
        return _format;
    }

    public void set_format(String _format) {
        this._format = _format;
    }

    public String get_responseType() {
        return _responseType;
    }

    public void set_responseType(String _responseType) {
        this._responseType = _responseType;
    }

    public String get_position() {
        return _position;
    }

    public void set_position(String _position) {
        this._position = _position;
    }
}
