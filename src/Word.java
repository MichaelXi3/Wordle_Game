public class Word {
    // Private variables
    private String word;
    private char[] charOfWord;

    // Constructor
    public Word (String word) {
        this.word = word;
    }

    //Mutators
    public void setWord (String word) {
        this.word = word;
    }
    public void setCharOfWordArray (char[] array) {
        charOfWord = array;
    }

    //Accessor
    public String getWord () {
        return word;
    }

    public char getCharAtIndex (int index) {
        return word.charAt(index);
    }

    public boolean checkIfHasChar (char character) {
        for (int i=0; i<word.length(); i++) {
            if (word.charAt(i) == character) {
                return true;
            }
        }
        return false;
    }
}
