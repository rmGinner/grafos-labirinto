#include <stdlib.h>
#include <stdio.h>

/* Para colocar o caminho usando pontos em vermelho: mudar o programa
 * para gerar comandos
 *
 * <circle cx="31.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 * <circle cx="32.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 * <circle cx="33.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 *
 */

void svgline( int x1, int y1, int x2, int y2 ) {
    printf(" <polyline points=\"%d,%d %d,%d\"/>\n", x1, y1, x2, y2);
}

void process ( int w, int h ) {

  int N = 8;
  int L = 4;
  int S = 2;
  int O = 1;

  int l = 0;
  int c = 0;

  while ( 1 ) {

    char t = ( char ) getc( stdin );

    if ( feof( stdin ) ) return;

    switch ( t ) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9': t -= '0'; break;
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f': t = t - 'a' + 10; break;
        default: continue;
    }

    if ( ( t & N ) == N ) svgline( c, l , c + 1, l);
    if ( ( t & S ) == S ) svgline( c, l + 1, c + 1, l + 1);
    if ( ( t & O ) == O ) svgline( c , l , c , l + 1);
    if ( ( t & L ) == L ) svgline( c + 1, l , c + 1, l + 1);

    c++;
    if ( c == w ) {
        c = 0;
        l++;
    }

  }
}

int main ( ) {

  int w, h;
  fscanf( stdin, "%d %d", &h, &w );

  puts ( "<?xml version=\"1.0\" standalone=\"no\"?>" );
  printf ( "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"%dcm\" height=\"%dcm\" viewBox=\"-0.1 -0.1 %f %f\">", w, h, w+0.2, h+0.2 );
  puts ( "<g style=\"stroke-width:.1; stroke:black; stroke-linejoin:miter; stroke-linecap:butt; \">" );

  process( w, h );

  // Ex: gerando circulos no labirinto
  puts ("<circle cx=\"11.5\" cy=\"0.5\" r=\"0.2\" stroke=\"red\" fill=\"red\" />");
  puts ("<circle cx=\"12.5\" cy=\"0.5\" r=\"0.2\" stroke=\"red\" fill=\"red\" />");
  puts ("<circle cx=\"13.5\" cy=\"0.5\" r=\"0.2\" stroke=\"red\" fill=\"red\" />");

  // Finaliza
  puts ( "</g>" );
  puts ( "</svg>" );
  return 0;
}
